package besucha.backend.service;

import besucha.backend.dao.SectionDao;
import besucha.backend.dao.StudentDao;
import besucha.backend.model.emailer.EmailAddress;
import besucha.backend.model.emailer.Emailer;
import besucha.backend.model.emailer.TrinityAuthenticator;
import besucha.backend.repo.StudentRepo;
import besucha.backend.service.accessdao.EnrolledService;
import besucha.backend.service.accessdao.WaitlistService;
import besucha.backend.service.parseexcel.EnrolledDataSaver;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Send emails to students.
 */
@Service
public class EmailSender {

	private WaitlistService waitlistService;
	private EnrolledService enrolledService;
	private StudentRepo studentRepo;

	public EmailSender(WaitlistService waitlistService, EnrolledService enrolledService, StudentRepo studentRepo) {
		this.waitlistService = waitlistService;
		this.enrolledService = enrolledService;
		this.studentRepo = studentRepo;
	}

	public String send(String username, String password) {
		try {
			Emailer mailer = new Emailer(new TrinityAuthenticator(username, password));
			String message = "";
			for(StudentDao studentDao : studentRepo.findAll()) {
				message = "Dear " + studentDao.getName();
				message += "\n\nBelow are your enrolled and waitlisted courses that were determined by BESUCHA.";
				message += "\n\nEnrolled:\n";
				List<SectionDao> sections = enrolledService.getEnrolledSections(studentDao.getStudentId());
				for (SectionDao sectionDao : sections) {
					message += sectionDao.getSectionId()+": " + sectionDao.getTitle() + "\n\n";

				}
				message += "\n\nWaitlisted:\n\n";
				List<SectionDao> wsections = waitlistService.getWaitlistedSectionsByStudent(studentDao.getStudentId());
				for (SectionDao sectionDao : wsections) {
					message += sectionDao.getSectionId()+": " + sectionDao.getTitle() + "\n\n";

				}
				message += "\n\nYour caring Registrars, \nEdwin, Bettina, and Logan";
				EmailAddress ea = new EmailAddress(studentDao.getEmail());

				mailer.sendEmail(ea, "Course Selection", message);
			}
		} catch (Exception e) {
			return e.getMessage();
		}

		return null;
	}
}
