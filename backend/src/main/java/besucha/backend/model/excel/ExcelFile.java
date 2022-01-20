package besucha.backend.model.excel;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.InvalidOperationException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

/**
 * Wrapper class for Apache's Excel spread sheet libraries
 */
public class ExcelFile implements List<XSSFSheet>, AutoCloseable {
    private XSSFWorkbook workbookFile;
    private String workbookName;

    /**
     * Constructs an empty Excel File
     */
    public ExcelFile() {
        this.workbookFile = new XSSFWorkbook();
        this.workbookName = "";
        this.workbookFile.createSheet();
        this.workbookFile.setActiveSheet(0);
    }

    /**
     * Opens an Excel File with the provided name/path, if it can be found.
     *
     * @param fileName the name of the Excel File
     * @throws IOException if the file can not be properly opened.
     * @throws InvalidFormatException if the provided file is not applicable to be an Excel File.
     */
    public ExcelFile(String fileName) throws IOException, InvalidFormatException {
        OPCPackage opcp;
        try {
            opcp = OPCPackage.open(fileName);
            this.workbookFile = new XSSFWorkbook(opcp);
        }
        catch (InvalidOperationException e){
            this.workbookFile = new XSSFWorkbook();
            this.workbookFile.createSheet();
        }

        this.workbookName = fileName;
    }

    /**
     * Opens the provided file as an Excel File
     * @param f the Excel File to open
     * @throws IOException if the file can not be properly opened/read from
     */
    public ExcelFile(File f) throws IOException {
        this(new FileInputStream(f));
        this.workbookName = f.getName();
    }

    /**
     * Constructs an Excel File from a pre-initialized File Stream
     * @param f the File String to draw from
     * @throws IOException if the file can not be properly read
     */
    private ExcelFile(FileInputStream f) throws IOException {
        this.workbookFile = new XSSFWorkbook(f);
        this.workbookName = "";
        this.workbookFile.setActiveSheet(0);
    }

    /**
     * Get the file name, but without all of the pathing details
     * @return String the file name
     */
    public String getRelativeFileName() {
        return FilenameUtils.getName(workbookName);
    }

    public File export() {
        File data = new File(System.getProperty("user.dir"), "Results.xlsx");
        data.deleteOnExit();
        FileOutputStream fos;

        try {
            fos = new FileOutputStream(data);
            this.workbookFile.write(fos);
        } catch (IOException e) {
            System.out.println("ERROR: " + e.toString());
        }
        return data;
    }


    /**
     * Saves the changes made to the Excel File
     * @throws IOException if the file could not be saved
     */
    public void save() throws IOException {
        String s = "";
        if (this.workbookName.equals(s)) this.save(this.promptForFileName());
        else this.save(this.workbookName);
    }

    /**
     * Saves the changes made to the Excel File
     * @throws IOException if the file could not be saved
     */
    public void saveAndClose() throws IOException {
        this.save();
        this.close();
    }

    /**
     * Returns an iterator over the sheets in this Excel File in proper sequence.
     * @return an iterator over the sheets in this Excel File in proper sequence.
     */
    // @Override
    public Iterator<XSSFSheet> iterator() {
        return this.listIterator(); // I cheat
    }

    /**
     * Returns an iterator referencing the rows in the active sheet of the Excel File
     * @return the row iterator in proper sequence
     */
    public Iterator<Row> activeSheetIterator() {
        return this.getActiveSheet().iterator();
    }

    /**
     * Returns the active sheet of the Excel File
     * @return the active sheet
     */
    public XSSFSheet getActiveSheet() {
        return this.get(this.workbookFile.getActiveSheetIndex());
    }

    /**
     * Sets the active sheet to the index provided
     * @param index the sheet number to change to
     */
    public void setActiveSheet(int index) {
        if (index < 0 || index > this.workbookFile.getNumberOfSheets())
            throw new IndexOutOfBoundsException();

        if (this.workbookFile.getActiveSheetIndex() != index)
            this.workbookFile.setActiveSheet(index);
    }

    /**
     * Closes the workbook
     */
//    // @Override
    public void close() throws IOException {
        this.workbookFile.close();
    }

    /**
     * Returns the active sheet
     * @return the currently active sheet
     */
    public XSSFSheet get(){
        return workbookFile.getSheetAt(workbookFile.getActiveSheetIndex());
    }

    /**
     * Sets the value of the specified cell in the active sheet
     * @param rowNum row index of the cell
     * @param columnNum column index of the cell
     * @param ct The Cell Type of the provided object
     * @param value the value to place into the cell
     */
    public void setCell(int rowNum, int columnNum, CellType ct, Object value){
        if (value == null) throw new NullPointerException("Value can not be empty");
        Sheet s = this.get();
        Row r = s.getRow(rowNum);
        if (r == null) r = s.createRow(rowNum);
        Cell c = r.getCell(columnNum);
        if (c == null) c = r.createCell(columnNum);
        this.setCell(c, ct, value);
    }

    /**
     * Sets the value of the specified cell in the active sheet
     * @param targetCell the cell to set the value of
     * @param ct the type of the provided value
     * @param value the value to place into the targetCell
     */
    public void setCell(Cell targetCell, CellType ct, Object value){
        if (!isTypeSafe(ct, value)) throw new RuntimeException(
            String.format("Type mismatch when setting cell:\nCellType and ValueType do not match.",
            targetCell.getRowIndex(), targetCell.getColumnIndex()));

        switch(ct) {
            case STRING:
            case FORMULA:{
                targetCell.setCellValue((String)value);
                return;
            }
            case NUMERIC: {
                if (value instanceof Double)
                    targetCell.setCellValue((Double)value);
                else
                    targetCell.setCellValue((Integer)value) ;
                return;
            }
            case BOOLEAN:{
                targetCell.setCellValue((Boolean)value);
                return;
            }
            case ERROR:{
                targetCell.setCellErrorValue((Byte)value);
                return;
            }
            case BLANK:{
                targetCell.setBlank();
                return;
            }
            default: throw new RuntimeException("Critical Error: Semantically unaccessable code accessed!");
        }
    }

    /**
     * Gets the value of the cell
     * @param targetCell
     * @return the value
     */
    public Object getCell(Cell targetCell){
        switch(targetCell.getCellType()) {
            case STRING: {
                return getCell(targetCell, new StringBuilder(), false);
            }
            case FORMULA: {
                return getCell(targetCell, new StringBuilder(), true);
            }
            case NUMERIC: {
                return getCell(targetCell, 0.0);
            }
            case BOOLEAN:{
                return getCell(targetCell, false);
            }
            case ERROR:{
                Byte b = null;
                return getCell(targetCell, b);
            }
            case BLANK:{
                return null;
            }
            default: return false;
        }
    }

    /**
     * Get the value of the cell specified and returns it as an Object
     * @param rowNum the row index of the cell
     * @param columnNum the column index of the cell
     * @return the value of the cell as a generic Object
     */
    public Cell getCell(int rowNum, int columnNum){
        Sheet s = this.get();
        Row r = s.getRow(rowNum);
        if (r == null) return null;
        Cell c = r.getCell(columnNum);
        return c;
    }

    /**
     * Compares the equality of the provided object to this instance
     *
     * @param obj the object to test the equality of
     * @return whether the object is equal to this instance
     */
    // @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (!(obj instanceof ExcelFile)) return false;
        ExcelFile ef = (ExcelFile)obj;
        if (ef.toString() == this.toString()){
            if (ef.size() != this.size()) return false;
            XSSFSheet[] thisArr = (XSSFSheet[])this.toArray(), efArr = (XSSFSheet[])ef.toArray();
            for (int i = 0; i < this.size(); i++){
                if (!thisArr[i].toString().equals(efArr[i].toString())) return false;
            }
            return true;
        }
        return false;
    }

//    /**
//     * Returns the name of the workbook (if known)
//     * @return name of the ExcelFile
//     */
//     @Override
//    public String toString(){
//        String str = this.workbookName + ":\n";
//        for (Sheet s : this) str += ("\t" + s.toString() + "\n");
//        return str;
//    }

    /**
     * Overrides the hashCode method to work with Excel Files
     * @return the hash code of the Excel File
     */
     @Override
    public int hashCode(){
        return this.toString().hashCode();
    }

    /**
     * Appends a new blank at the end of the Excel File
     * @return the reference to the blank file
     */
    public XSSFSheet add(){
        int activeSheetIndex = this.workbookFile.getActiveSheetIndex();
        XSSFSheet s = this.workbookFile.createSheet(); // Automatically added to spreadsheet
        this.workbookFile.setActiveSheet(activeSheetIndex);
        return s;
    }

    /**
     * Appends the specified sheet at the end of the Excel File
     * @throws NullPointerException if the sheet provided is null;
     * @return whether the list has been modified (will return true unless exception is raised)
     */
//     @Override
    public boolean add(XSSFSheet sheetToAdd) throws NullPointerException {
        if (sheetToAdd == null) throw new NullPointerException("Sheet to add to workbook may not be null.");
        int activeSheetIndex = this.workbookFile.getActiveSheetIndex();
        this.copySheetOver(workbookFile, sheetToAdd);
        this.workbookFile.setActiveSheet(activeSheetIndex);
        return true;
    }

    /**
     * Inserts the specified sheet at the specified index of the Excel File
     * @param index the index to insert the sheet at
     * @param sheet the sheet to add
     * @throws NullPointerException if the provided sheet is null
     * @throws IndexOutOfBoundsException if the index doesn't fit the Excel File
     */
    // @Override
    public void add(int index, XSSFSheet sheet) throws NullPointerException, IndexOutOfBoundsException {
        if (sheet == null)
            throw new NullPointerException("Sheet to add to workbook may not be null.");
        if (this.size() <= index || index < 0)
            throw new IndexOutOfBoundsException();
        this.add(sheet);
        this.workbookFile.setSheetOrder(sheet.getSheetName(), index);
    }

    /**
     * Adds the provided collection of sheets to the Excel File
     * @param sheetCollection the collection of sheets to add to the Excel File
     * @return whether or not the Excel File was mutated
     */
    // @Override
    public boolean addAll(Collection<? extends XSSFSheet> sheetCollection) {
        if (sheetCollection.isEmpty()) return false;

        for (XSSFSheet s : sheetCollection) this.add(s);

        return true;
    }

    /**
     * Inserts the collection of sheets at the specified index of the Excel File
     * @param index the index to insert the sheet at
     * @param sheetCollection the sheet to add
     * @throws IndexOutOfBoundsException if the index doesn't fit the Excel File
     */
    // @Override
    public boolean addAll(int index, Collection<? extends XSSFSheet> sheetCollection) throws IndexOutOfBoundsException {
        if (sheetCollection.isEmpty()) return false;
        int i = index;

        for (XSSFSheet s : sheetCollection) this.add(i++, s);

        return true;
    }

    /**
     * Empties the workbook, leaving a single empty, active page
     * @apiNote workbook can never actually be empty
     */
    // @Override
    public void clear() {
        this.workbookFile.createSheet();
        while(this.workbookFile.getNumberOfSheets() > 1){
            this.remove(0);
        }
    }

    /**
     * Empties the workbook and adds the provided sheet as the active sheet
     * @apiNote workbook can never actually be empty
     * @param s
     */
    public void clear(XSSFSheet s) {
        this.add(s);
        while(this.workbookFile.getNumberOfSheets() > 1){
            this.remove(0);
        }
    }

    /**
     * Returns true if this list contains the specified sheet.
     * More formally, returns true if and only if this list contains at least one sheet 's' such that Objects.equals(o, s).
     * @param obj the sheet/object whose presence in this list is to be tested
     * @return true if this list contains the specified sheet
     */
     @Override
    public boolean contains(Object obj) {
        for (XSSFSheet s: this){
            if (s.equals(obj)){
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if this list contains all the specified sheet in the provided collection.
     * More formally, returns true if and only if this list contains at least one sheet 's' such that Objects.equals(o, s).
     * @param c the collection of sheets/objects whose presence in this list is to be tested
     * @return true if this list contains the specified sheet
     */
    // @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c){
            if (!this.contains(o)){
                return false;
            }
        }
        return true;
    }

    /**
     *
     */
    // @Override
    public XSSFSheet get(int index) throws IndexOutOfBoundsException {
        if (this.size() <= index || index < 0) throw new IndexOutOfBoundsException();
        return this.workbookFile.getSheetAt(index);
    }

    // @Override
    public int indexOf(Object obj) {
        for (int i = 0; i < this.size(); i++){
            if ((this.get(i)).equals(obj)) return i;
        }
        return -1;
    }

    /**
     * Returns false, because Excel Files need at least one sheet.
     */
    // @Override
    public boolean isEmpty() {
        // an XSSFworkbook can never be empty
        return false;
    }

    // @Override
    public int lastIndexOf(Object obj) {
        for (int i = this.size() - 1; i >= 0; i++){
            if ((this.get(i)).equals(obj)) return i;
        }
        return -1;
    }

    // @Override
    public ListIterator<XSSFSheet> listIterator() {
        return new XSSFSheetIterator();
    }

    // @Override
    public ListIterator<XSSFSheet> listIterator(int index) {
        return new XSSFSheetIterator(index);
    }

    /**
     * Removes the sheet from the Excel File (if it exists)
     * @param obj the sheet/object to remove
     * @return whether the Excel File was mutated
     */
    // @Override
    public boolean remove(Object obj) {
        if (this.size() == 1) this.workbookFile.createSheet(); // Dummy Sheet
        for (int i = 0; i < this.size(); i++){
            if (this.get(i).equals(obj)){
                workbookFile.removeSheetAt(i);
                return true;
            }
        }
        // If we didn't get a match, remove the dummy sheet we add to
        // make sure we don't violate the not-empty rule
        workbookFile.removeSheetAt(this.size() - 1);
        return false;
    }

    /**
     * Removes the sheet at the provided index from the Excel File (if it exists)
     * @param index the index of the sheet to remove
     * @throws IndexOutOfBoundsException if no sheet exists at the index
     * @return whether the Excel File was mutated
     */
    // @Override
    public XSSFSheet remove(int index) {
        if (index < 0 || index >= this.size()) throw new IndexOutOfBoundsException();
        if (this.size() == 1) this.workbookFile.createSheet(); // Dummy Sheet
        XSSFSheet tmp = this.get(index);
        workbookFile.removeSheetAt(index);
        return tmp;
    }

    /**
     * Removes each element in the provided collection from the Excel File (if they exist)
     * @param elements the list of elements to add
     * @return if the Excel File has been mutated
     */
    // @Override
    public boolean removeAll(Collection<?> elements) {
        boolean hasChanged = false;
        for(Object obj : elements){
            if (this.remove(obj))
                hasChanged = true;
        }
        return hasChanged;
    }

    /**
     * Removes sheets from the Excel File if they're not contained in the provided colection
     * @param elements the collection of sheets to retain
     * @return whether the list has been mutated
     */
    // @Override
    public boolean retainAll(Collection<?> elements) {
        boolean hasChanged = false;
        for(XSSFSheet s : this){
            if (!elements.contains(s)){
                hasChanged = true;
                this.remove(s);
            }
        }
        return hasChanged;
    }

    /**
     * Sets the value of the reference at the speicified index to be the provided sheet
     * @param index the index to place the inbound sheet into
     * @param sheet the sheet to set as
     * @throws IndexOutOfBoundsException if the index is out of bounds
     * @throws NullPointerException if the provided sheet is null
     * @return the reference to the freshly set sheet
     */
    // @Override
    public XSSFSheet set(int index, XSSFSheet sheet) {
        if (index < 0 || index >= this.size()) throw new IndexOutOfBoundsException();
        if (sheet == null) throw new NullPointerException("Provided sheet can not be null.");
        XSSFSheet tmp = workbookFile.getSheetAt(index);
        this.add(index, sheet);
        this.remove(index + 1);
        return tmp;
    }

    /**
     * Returns the number of elements in this list. If this list contains more than Integer.MAX_VALUE elements, returns Integer.MAX_VALUE
     * @return the size as an int
     */
    // @Override
    public int size() {
        return this.workbookFile.getNumberOfSheets();
    }

    /**
     *
     * @param fromIndex
     * @param toIndex
     * @throws IndexOutOfBoundsException if the index is out of bounds
     * @return the list of all sheets >= fromIndex && < toIndex
     */
     @Override
    public List<XSSFSheet> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > this.size() || fromIndex > toIndex) throw new IndexOutOfBoundsException();
        ExcelFile subWorkbook = new ExcelFile();
        for (int i = fromIndex; i < toIndex; i++){
            subWorkbook.add(this.get(i));
        }
        return subWorkbook;
    }

    /**
     * Returns an array containing all of the elements in this list in proper sequence (from first to last element).
     *
     * The returned array will be "safe" in that no references to it are maintained by this list.
     * (In other words, this method must allocate a new array even if this list is backed by an array). The caller is thus free to modify the returned array.
     *
     * This method acts as bridge between array-based and collection-based APIs.
     * @return an array containing all of the sheets in proper sequence.
     */
    // @Override
    public Object[] toArray() {
        XSSFWorkbook tmpWorkbook =  new XSSFWorkbook();
        Object[] array = new Object[this.size()];

        for (int i = 0; i < tmpWorkbook.getNumberOfSheets(); i++){
            this.copySheetOver(tmpWorkbook, this.get(i));
            array[i] = tmpWorkbook.getSheetAt(i);
        }
        return array;
    }

    /**
     * Returns an array containing all of the elements in this list in proper sequence (from first to last element);
     * the runtime type of the returned array is that of the specified array.
     * If the list fits in the specified array, it is returned therein.
     *
     * Otherwise, a new array is allocated with the runtime type of the specified array and the size of this list.
     * If the list fits in the specified array with room to spare (i.e., the array has more elements than the list),
     * the element in the array immediately following the end of the list is set to null.
     *
     * Like the toArray() method, this method acts as bridge between array-based and collection-based APIs.
     * Further, this method allows precise control over the runtime type of the output array, and may, under certain circumstances,
     * be used to save allocation costs.
     * @param arr the array to store the values if possible
     * @throws NullPointerException if the provided array is null
     * @throws ArrayStoreException if arr can not hold XSSFSheets
     * @return an array containing all of the sheets in proper sequence.
     */
    // @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] arr) {
        if (arr == null) throw new NullPointerException();
        if (!(arr instanceof XSSFSheet[])) throw new ArrayStoreException();

        if (arr.length >= this.size()){
            if (arr.length > this.size()) arr[this.size()] = null;
            for (XSSFSheet s : this) arr[this.indexOf(s)] = (T)s;
            // Can't avoid warning; no worries about cast b/c instanceof test above
            return arr;
        } else {
            // Can't avoid warning; no worries about cast b/c instanceof test above
            return (T[]) this.toArray();
        }
    }

    private String getCell(Cell targetCell, StringBuilder value, Boolean isFormula){
        String s = isFormula? targetCell.getCellFormula() : targetCell.getStringCellValue();
        value.append(s);
        return s;
    }

    private Double getCell(Cell targetCell, Double value){
        return targetCell.getNumericCellValue();
    }

    private Byte getCell(Cell targetCell, Byte value){
        return targetCell.getErrorCellValue();
    }

    private Boolean getCell(Cell targetCell, Boolean value){
        return targetCell.getBooleanCellValue();
    }

    private boolean isTypeSafe(CellType ct, Object value){
        switch(ct) {
            case STRING:
            case FORMULA: {
                return value instanceof String;
            }
            case NUMERIC: {
                return (value instanceof Double || value instanceof Integer);
            }
            case BOOLEAN:{
                return value instanceof Boolean;
            }
            case ERROR:{
                return value instanceof Byte;
            }
            case BLANK:{
                return true;
            }
            default: return false;
        }
    }

    private void copyOverCellValue(Cell targetCell, Cell originCell){
        switch(originCell.getCellType()) {
            case STRING: {
                this.setCell(targetCell, originCell.getCellType(), originCell.getStringCellValue());
                break;
            }
            case NUMERIC: {
                this.setCell(targetCell, originCell.getCellType(), originCell.getNumericCellValue());
                break;
            }
            case BOOLEAN:{
                this.setCell(targetCell, originCell.getCellType(), originCell.getBooleanCellValue());
                break;
            }
            case FORMULA:{
                this.setCell(targetCell, originCell.getCellType(), originCell.getCellFormula());
                break;
            }
            case ERROR:{
                this.setCell(targetCell, originCell.getCellType(), originCell.getErrorCellValue());
                break;
            }
            default: break;
        }
        return;
    }

    private void copySheetOver(Workbook targetBook, Sheet originSheet){
        Sheet targetSheet = targetBook.createSheet(originSheet.getSheetName());
        for (Row r : originSheet){
            Row rowRef = targetSheet.createRow(r.getRowNum());
            for (Cell c : r){
                Cell cellRef = rowRef.createCell(c.getColumnIndex(), c.getCellType());
                this.copyOverCellValue(cellRef, c);
            }
        }
    }

    private void save(String fileName) throws FileNotFoundException, IOException{
        FileOutputStream fos = new FileOutputStream(fileName);
        this.workbookFile.write(fos);
    }

    private String promptForFileName(){
        String str;
        Scanner s = new Scanner(System.in);

        System.out.println("Enter file name to save to (with extension):");
        str = s.nextLine();
        s.close();
        return str;
    }

    /** Untested Class */
    private class XSSFSheetIterator implements ListIterator<XSSFSheet> {
        int cursor;
        boolean canMutate;

        /**
         * Standard Constructor for the Iterator
         */
        public XSSFSheetIterator(){
            this(-1);
        }

        /**
         * Creates a sheet iterator starting at the specified index
         * @param index the starting index (note, default is -1)
         * @throws IndexOutOfBoundsException if the index is out of bounds
         */
        public XSSFSheetIterator(int index){
            if (index >= ExcelFile.this.size()) throw new IndexOutOfBoundsException();
            this.cursor = index;
            this.canMutate = false;
        }

        /**
         * Adds a sheet to the workbook through the iterator
         * @param sheet the sheet to add to the workbook
         */
        // @Override
        public void add(XSSFSheet sheet) {
            this.canMutate = false;
            ExcelFile.this.add(cursor++, sheet);
        }

        /**
         * Checks if there's more sheets to iterate through
         * @return if there's more sheets to iterate through
         */
        // @Override
        public boolean hasNext() {
            if (cursor +1 < workbookFile.getNumberOfSheets()) return true;
            return false;
        }

        /**
         * Checks if there's sheet to go back to
         * @return if there's sheet to reverse-iterate through
         */
        // @Override
        public boolean hasPrevious() {
            if (cursor < 0) return false;
            return true;
        }

        /**
         * Gets the next sheet in the iterator's order
         * @throws IndexOutOfBoundsException if there is no next sheet
         * @return the reference to the next sheet
         */
        // @Override
        public XSSFSheet next() {
            if(!this.hasNext()) throw new IndexOutOfBoundsException();
            this.canMutate = true;
            return ExcelFile.this.get(++cursor);
        }

        /**
         * Gets the index of the next sheet
         * @throws IndexOutOfBoundsException if there is no next sheet
         * @return the index of the next sheet
         */
        // @Override
        public int nextIndex() {
            if(!this.hasNext()) throw new IndexOutOfBoundsException();
            return cursor +1;
        }

        /**
         * Gets the previous sheet in the iterator's order
         * @throws IndexOutOfBoundsException if there is no previous sheet
         * @return reference to the previous sheet
         */
        // @Override
        public XSSFSheet previous() {
            if(!this.hasNext()) throw new IndexOutOfBoundsException();
            this.canMutate = true;
            return ExcelFile.this.get(--cursor);
        }

        /**
         * Gets the index of the previous sheet
         * @throws IndexOutOfBoundsException if there is no previous sheet
         * @return the index of the previous sheet
         */
        // @Override
        public int previousIndex() {
            if(!this.hasPrevious()) throw new IndexOutOfBoundsException();
            return cursor - 1;
        }

        /**
         * Removes the sheet most recently iterated to
         * @throws IllegalStateException if multiple remove/add operations occur per iteration
         * @see ListIterator< T >.remove(), ListIterator<T>.add(), ListIterator<T>.set(),
         */
        // @Override
        public void remove() {
            if (!this.canMutate) throw new IllegalStateException();
            int indexToRemove = cursor;
            this.canMutate = false;
            if (!this.hasNext() && this.hasPrevious()) cursor--;
            ExcelFile.this.remove(indexToRemove);
        }

        /**
         * Sets the value sheet most recently iterated to
         * @param sheet the sheet to set with
         * @throws IllegalStateException if multiple remove/set/add operations occur per iteration
         * @see ListIterator<T>.remove(), ListIterator<T>.add(), ListIterator<T>.set(),
         */
        // @Override
        public void set(XSSFSheet sheet) {
            if (!this.canMutate) throw new IllegalStateException();
            ExcelFile.this.set(cursor, sheet);
        }
    }
}
