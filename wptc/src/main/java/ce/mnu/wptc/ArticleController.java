package ce.mnu.wptc;

import ce.mnu.wptc.domain.ArticleDTO;
import ce.mnu.wptc.domain.FileDTO;
import ce.mnu.wptc.repository.Article;
import ce.mnu.wptc.repository.ArticleHeader;
import ce.mnu.wptc.repository.FileRepository;
import ce.mnu.wptc.repository.SiteFile;
import ce.mnu.wptc.service.SiteUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/siteuser/*")
public class ArticleController {
	@Autowired
	private SiteUserService userService;
	
	@Autowired
	private FileRepository fileRepository;
	
	@GetMapping("bbs/upload")
	public String visitUpload() {
		return "/bbs/upload_form";
	}
	
	@PostMapping("bbs/upload")
	public String upload(@RequestParam MultipartFile file, Model model) throws IllegalStateException, IOException {
		if(!file.isEmpty()) {
			String fileName = file.getOriginalFilename();
			fileName = fileName.replace(' ', '_');
			FileDTO dto = new FileDTO();
			dto.setFileName(fileName);
			dto.setContentType(file.getContentType());
			File upfile = new File(dto.getFileName());
			file.transferTo(upfile);
			
			SiteFile siteFile = new SiteFile();
			siteFile.setName(fileName);
			fileRepository.save(siteFile);
			model.addAttribute("file", dto);
		}		
		return "/bbs/result";
	}
	
	@GetMapping("bbs/filelist")
	public String fileList(Model model) {
		Iterable<SiteFile> files = fileRepository.findAll();
		model.addAttribute("files", files);
		return "/bbs/file_list";
	}
	
	@Value("${spring.servlet.multipart.location}")
	String base;
	@GetMapping("bbs/download")
	public ResponseEntity<Resource> download(FileDTO dto) throws IOException {
		Path path = Paths.get(base+"/"+dto.getFileName());
		String contentType = Files.probeContentType(path);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentDisposition(
			ContentDisposition.builder("attachment")
			.filename(dto.getFileName(), StandardCharsets.UTF_8)
			.build()
		);
		headers.add(HttpHeaders.CONTENT_TYPE, contentType);
		Resource res = new InputStreamResource(Files.newInputStream(path));
		return new ResponseEntity<>(res, headers, HttpStatus.OK);
	}
	
	@GetMapping("bbs/read")
	public String readArticle(@RequestParam Long num, 
			Model model, HttpSession session) {
		Article article = userService.getArticle(num);
		model.addAttribute(article);
		return "/bbs/article";
	}
	
	@GetMapping("bbs/write")
	public String bbsForm() {
		return "/bbs/new_article";
	}
	@PostMapping("bbs/write")
	public String addArticle(ArticleDTO dto) {
		userService.save(dto);
		return "/bbs/saved";
	}
	@GetMapping("bbs")
	public String getAllArticles(
			@RequestParam(defaultValue = "0") int pno,
			Model model, HttpSession session, 
			RedirectAttributes rd) {
		String email = (String) session.getAttribute("email");
		if(email == null) {
			rd.addFlashAttribute("reason", "login required");
			return "redirect:/error";
		}
		int pageSize = 5;
		Pageable paging = PageRequest.of(pno, pageSize,
				Sort.Direction.DESC, "num");
		Page<ArticleHeader> list = userService.getArticleHeaders(paging);
		model.addAttribute("articles", list);
		return "/bbs/articles";
	}
}
