package ce.mnu.wptc;

import ce.mnu.wptc.domain.SiteUserDTO;
import ce.mnu.wptc.repository.SiteUser;
import ce.mnu.wptc.service.SiteUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/siteuser/*")
public class SiteUserController {
	@Autowired
	private SiteUserService userService;

	@GetMapping("/signup")
	public String signup() {
		return "signup_input";
	}

	@PostMapping("/signup")
	public String signup(SiteUserDTO user) {
		userService.save(user);
		return "signup_done";
	}
	@GetMapping("/all")
	@ResponseBody
	public Iterable<SiteUser> getAllusers() {
		return userService.getAll();
	}
	@GetMapping("/find")
	public String find() {
		return "find_user";
	}
	@PostMapping("/find")
	public String findUser(@RequestParam String email,
			Model model, RedirectAttributes rd) {
		SiteUser user = userService.getEmail(email);
		userService.updateName(email, "홍길동");
		if(user != null) {
			model.addAttribute("user", user);
			return "find_done";
		}
		rd.addFlashAttribute("reason", "wrong email");
		return "redirect:/error";
	}
	@GetMapping("/")
	public String start() {
		return "start";
	}
	@GetMapping("/login")
	public String loginForm() {
		return "login";
	}
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "login";
	}
	@PostMapping("/login")
	public String loginUser(SiteUserDTO dto, HttpSession session,
			RedirectAttributes rd) {
		SiteUser user = userService.getEmail(dto.getEmail());
		if(user != null) {
			if(dto.getPasswd().equals(user.getPasswd())) {
				session.setAttribute("email", dto.getEmail());
				return "login_done";
			}
		}
		rd.addFlashAttribute("reason", "wrong password");
		return "redirect:/error";
	}
}
