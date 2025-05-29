package ce.mnu.wptc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import ce.mnu.wptc.service.UserService;

@Controller
@RequestMapping("/wptc/*")
public class UserController {
	
	@Autowired
	UserService userService;
	
	
	
}
