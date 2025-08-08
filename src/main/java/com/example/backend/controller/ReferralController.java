package com.example.backend.controller;


import com.example.backend.service.ReferralNetworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/referrals")
public class ReferralController {

    @Autowired
    private ReferralNetworkService referralService;

    @PostMapping("/add")
    public String addReferral(@RequestParam String referrerId, @RequestParam String candidateId) {
        boolean success = referralService.addReferral(referrerId, candidateId);
        return success ? "Referral added" : "Invalid referral";
    }

    @GetMapping("/{userId}")
    public List<String> getReferrals(@PathVariable String userId) {
        return referralService.getDirectReferrals(userId);
    }

    @GetMapping("/reach/{userId}")
    public int getTotalReach(@PathVariable String userId) {
        return referralService.getTotalReach(userId);
    }

    @GetMapping("/top-referrers")
    public List<String> getTopReferrers(@RequestParam int k) {
        return referralService.getTopReferrers(k);
    }

    @GetMapping("/influencers/unique-reach")
    public List<String> getTopUnique(@RequestParam int k) {
        return referralService.getTopUniqueExpanders(k);
    }

    @GetMapping("/influencers/flow-centrality")
    public Map<String, Integer> getFlowCentrality() {
        return referralService.getFlowCentralityScores();
    }


}

