package com.example.backend.controller;

import com.example.backend.service.NetworkSimulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.function.Function;

@RestController
@RequestMapping("/api/simulate")
public class SimulationController {

    @Autowired
    private NetworkSimulationService simulationService;

    @GetMapping("/growth")
    public List<Double> simulateGrowth(
            @RequestParam double p,
            @RequestParam int days
    ) {
        return simulationService.simulate(p, days);
    }

    @GetMapping("/days-to-target")
    public int getDaysToTarget(
            @RequestParam double p,
            @RequestParam int target
    ) {
        return simulationService.daysToTarget(p, target);
    }

    @GetMapping("/min-bonus")
    public Integer getMinBonus(
            @RequestParam int days,
            @RequestParam int target,
            @RequestParam double eps
    ) {
        Function<Double, Double> adoptionProb = bonus -> Math.min(0.0001 * bonus, 1.0);
        return simulationService.minBonusForTarget(days, target, adoptionProb, eps);
    }

}
