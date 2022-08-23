package Togefit.server.controller;

import Togefit.server.model.IdInfo;
import Togefit.server.model.RoutineInfo;
import Togefit.server.model.RoutineListInfo;
import Togefit.server.response.OperationResponse;
import Togefit.server.service.RoutineService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RequestMapping("/routines")
@RestController
public class RoutineController {

    final private RoutineService routineService;

    public RoutineController(RoutineService routineService) {
        this.routineService = routineService;
    }

    @PostMapping("/")
    public OperationResponse register(@RequestBody RoutineListInfo RoutineListInfo, HttpServletRequest request){
        OperationResponse resp = new OperationResponse();

        String userId = (String) request.getAttribute("userId");

        resp.setResult(routineService.saveRoutineList(RoutineListInfo, userId) + "가(이) 등록되었습니다.");
        return resp;
    }

    @GetMapping("/")
    public RoutineInfo getRoutine(HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");

        return routineService.getRoutine(userId);
    }

    @DeleteMapping("/")
    public OperationResponse deleteRoutine(@RequestBody IdInfo idInfo, HttpServletRequest request){
        OperationResponse resp = new OperationResponse();
        String userId = (String) request.getAttribute("userId");
        routineService.deleteRoutine(userId, idInfo.getId());
        resp.setResult("정상적으로 루틴이 삭제되었습니다.");
        return resp;
    }

    @GetMapping("/search")
    public RoutineInfo searchRoutine(@RequestParam("routineName") String routineName, HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        System.out.println(userId);
        return routineService.searchRoutine(userId, routineName);
    }

}
