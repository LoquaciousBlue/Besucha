package besucha.backend.service.parseexcel;

import besucha.backend.dao.SectionDao;
import besucha.backend.dao.StudentDao;
import besucha.backend.model.excel.ExcelFile;
import besucha.backend.repo.StudentRepo;
import besucha.backend.service.accessdao.EnrolledService;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

/**
 * Retrieves enrollment data and creates ExcelFile.
 */
@Service
public class EnrolledDataSaver {

	StudentRepo studentRepo;
	EnrolledService enrolledService;

	public EnrolledDataSaver(StudentRepo studentRepo, EnrolledService enrolledService) {
		this.studentRepo = studentRepo;
		this.enrolledService = enrolledService;
	}

	/**
	 * After enrollment data has been saved, create and return the Excel File.
	 * @return File object
	 */
	public File createExcel() {
		ExcelFile ef = new ExcelFile();
		ef.setActiveSheet(0);
		ef.getActiveSheet();
		int i = 2;
		//SETUP FORMAT
		ef.setCell(0,0, CellType.STRING,"Results of BESUCHA ALGORITHM");
		ef.setCell(1,0, CellType.STRING, "STUDENT_ID");
		ef.setCell(1, 1, CellType.STRING,"NAME");
		ef.setCell(1, 2, CellType.STRING,"EMAIL");
		ef.setCell(1, 3, CellType.STRING, "SECTIONS");


		for(StudentDao studentDao : studentRepo.findAll()) {
			ef.setCell(i, 0, CellType.NUMERIC, studentDao.getStudentId());
			ef.setCell(i, 1, CellType.STRING, studentDao.getName());
			ef.setCell(i, 2, CellType.STRING, studentDao.getEmail());
			List<SectionDao> sections = enrolledService.getEnrolledSections(studentDao.getStudentId());
			int j = 3;
			for (SectionDao sectionDao : sections) {
				ef.setCell(i, j, CellType.NUMERIC, sectionDao.getSectionId());
				j++;
			}
			i++;
		}

		return ef.export();
	}
}
