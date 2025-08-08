package com.example.backend.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Service
public class NetworkSimulationService {

    private final int INITIAL_REFERRERS = 100;
    private final int REFERRAL_CAPACITY = 10;

    public List<Double> simulate(double p, int days) {
        List<Double> cumulativeReferrals = new ArrayList<>();
        double totalReferrals = 0;
        double activeReferrers = INITIAL_REFERRERS;
        double[] capacities = new double[days + 1];  // running estimate of new referrals each day

        for (int day = 0; day <= days; day++) {
            // Expected successful referrals today = activeReferrers * p
            double expectedToday = activeReferrers * p;

            if (expectedToday > activeReferrers * REFERRAL_CAPACITY) {
                expectedToday = activeReferrers * REFERRAL_CAPACITY;
            }

            totalReferrals += expectedToday;
            cumulativeReferrals.add(totalReferrals);

            // Update active referrers for next day
            activeReferrers += expectedToday;
            if (activeReferrers > INITIAL_REFERRERS * REFERRAL_CAPACITY) {
                activeReferrers = INITIAL_REFERRERS * REFERRAL_CAPACITY; // max out
            }
        }

        return cumulativeReferrals;
    }

    public int daysToTarget(double p, int targetTotal) {
        double totalReferrals = 0;
        double activeReferrers = INITIAL_REFERRERS;
        int day = 0;

        while (totalReferrals < targetTotal && day < 10000) { // safety limit
            double expectedToday = activeReferrers * p;
            totalReferrals += expectedToday;
            activeReferrers += expectedToday;

            if (activeReferrers > INITIAL_REFERRERS * REFERRAL_CAPACITY) {
                activeReferrers = INITIAL_REFERRERS * REFERRAL_CAPACITY;
            }

            day++;
        }

        return (totalReferrals >= targetTotal) ? day : -1;
    }

    public Integer minBonusForTarget(
            int days,
            int targetHires,
            Function<Double, Double> adoptionProb,
            double eps
    ) {
        int low = 0;
        int high = 10000; // max bonus $10,000
        Integer answer = null;

        while (low <= high) {
            int mid = (low + high) / 2;

            double p = adoptionProb.apply((double) mid);
            List<Double> simResult = simulate(p, days);
            double total = simResult.get(simResult.size() - 1);

            if (total >= targetHires - eps) {
                answer = mid;
                high = mid - 10; // try smaller
            } else {
                low = mid + 10; // need more bonus
            }
        }

        return answer;
    }

}
