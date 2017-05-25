package com.basant.process.excel.api.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.basant.process.excel.api.pojo.Employee;
import com.basant.process.excel.api.service.ExcelExtractorService;

@Controller
public class UploadController {

	private static String UPLOADED_FILENAME = "";
	// Save the uploaded file to this folder
	private static String UPLOADED_FOLDER = "C:/Users/basanta.kumar.hota/Desktop/Dummy/";
	@Autowired(required = true)
	private ExcelExtractorService service;

	@GetMapping("/")
	public String index() {
		return "upload";
	}

	@PostMapping("/upload")
	public String singleFileUpload(@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes) {
		if (file.isEmpty() || !file.getOriginalFilename().endsWith(".xlsx")) {
			redirectAttributes
					.addFlashAttribute("message",
							"Please select a file to upload and it should be Excel Type as per code");
			return "redirect:uploadStatus";
		}
		try {
			// Get the file and save it somewhere
			byte[] bytes = file.getBytes();
			Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
			Files.write(path, bytes);

			redirectAttributes.addFlashAttribute("message",
					"You successfully uploaded '" + file.getOriginalFilename()
							+ "'");
			UPLOADED_FILENAME = file.getOriginalFilename();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "redirect:/uploadStatus";
	}

	@GetMapping("/uploadStatus")
	public String uploadStatus() {
		return "uploadStatus";
	}

	@RequestMapping("/viewList")
	public String viewRecords(Model model) throws Exception {
		List<Employee> employees = service.excelToJavaBean(UPLOADED_FOLDER
				+ UPLOADED_FILENAME);
		model.addAttribute("employees", employees);
		return "uploadStatus";

	}
}