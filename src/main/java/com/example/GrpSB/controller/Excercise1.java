package com.example.GrpSB.controller;


import com.example.GrpSB.model.Attachment;
import com.example.GrpSB.model.Euser;
import com.example.GrpSB.model.Excercise;
import com.example.GrpSB.model.Stock;
import com.example.GrpSB.service.ExcerciseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/worksheet1")
public class Excercise1 {

    private ExcerciseService excerciseService;


    public Excercise1(ExcerciseService excerciseService) {
        this.excerciseService = excerciseService;
    }

    @PostMapping(value="/excercise")
    public String postExcercise(@RequestBody Excercise excercise) {
        String path = MessageFormat.format("redirect:/{0}/ex{1}",excercise.getWorksheet(),
                excercise.getTask());
        return path;
    }

   @GetMapping(value = "/ex2")
    public String getEx2(Model model) {
        Map<String,List> tasks = excerciseService.getTask2();
        model.addAttribute("A", tasks.get("A"));
        model.addAttribute("B", tasks.get("B"));
        model.addAttribute("C", tasks.get("C"));
        model.addAttribute("D", tasks.get("D"));
        model.addAttribute("E", tasks.get("E"));
        model.addAttribute("F", tasks.get("F"));
        model.addAttribute("G", tasks.get("G"));
        model.addAttribute("H", tasks.get("H"));
        return "ex2";
    }

    @GetMapping(value = "/ex3")
    public String getEx3() {
        return "S1Ex3";
    }

    @PostMapping(value="/showaddressbook")
    public String showAddress(@RequestBody Euser euser, Model model) {
        List<Map<String,String>> addrbook = excerciseService.getAddressbook(euser.getEuid());
        model.addAttribute("A", addrbook);
        return "ex3";
    }

    @GetMapping(value = "/ex4")
    public String getEx4() {
        return "S1Ex4";
    }

    @PostMapping(value="/getAttachments")
    public String showAttachments(@RequestBody Attachment attachment, Model model) {
        List<Map<String,String>> result = excerciseService.getTask4(attachment.getNickname(),attachment.getFrom_date(),
                attachment.getTo_date(),attachment.getQuery());
        model.addAttribute("A", result);
        return "ex4";
    }


}
