package com.example.backend.service;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

public class ReferralNetworkServiceTest {

    @Test
    void testValidReferral() {
        ReferralNetworkService service = new ReferralNetworkService();
        assertTrue(service.addReferral("A", "B"));
        List<String> referrals = service.getDirectReferrals("A");
        assertEquals(1, referrals.size());
        assertTrue(referrals.contains("B"));
    }

    @Test
    void testSelfReferral() {
        ReferralNetworkService service = new ReferralNetworkService();
        assertFalse(service.addReferral("A", "A"));
    }

    @Test
    void testDuplicateReferral() {
        ReferralNetworkService service = new ReferralNetworkService();
        service.addReferral("A", "B");
        assertFalse(service.addReferral("C", "B")); // Already referred
    }

    @Test
    void testCycleReferral() {
        ReferralNetworkService service = new ReferralNetworkService();
        service.addReferral("A", "B");
        service.addReferral("B", "C");
        assertFalse(service.addReferral("C", "A")); // should reject cycle
    }

    @Test
    void testTotalReach() {
        ReferralNetworkService service = new ReferralNetworkService();
        service.addReferral("A", "B");
        service.addReferral("B", "C");
        service.addReferral("C", "D");

        assertEquals(3, service.getTotalReach("A"));
        assertEquals(2, service.getTotalReach("B"));
        assertEquals(1, service.getTotalReach("C"));
        assertEquals(0, service.getTotalReach("D"));
    }

    @Test
    void testTopReferrers() {
        ReferralNetworkService service = new ReferralNetworkService();
        service.addReferral("A", "B");
        service.addReferral("A", "C");
        service.addReferral("B", "D");
        service.addReferral("C", "E");

        List<String> top2 = service.getTopReferrers(2);
        assertEquals("A", top2.get(0)); // A should be top
    }

    @Test
    void testUniqueReachExpansion() {
        ReferralNetworkService service = new ReferralNetworkService();
        service.addReferral("A", "B");
        service.addReferral("A", "C");
        service.addReferral("B", "D");
        service.addReferral("C", "E");

        List<String> top = service.getTopUniqueExpanders(2);
        assertTrue(top.contains("A"));
    }

    @Test
    void testFlowCentralityBasic() {
        ReferralNetworkService service = new ReferralNetworkService();
        service.addReferral("A", "B");
        service.addReferral("B", "C");
        service.addReferral("A", "D");
        service.addReferral("D", "C");

        Map<String, Integer> flow = service.getFlowCentralityScores();
        assertTrue(flow.get("B") > 0 || flow.get("D") > 0);
    }

    @Test
    void testSimulationGrowth() {
        NetworkSimulationService sim = new NetworkSimulationService();
        List<Double> results = sim.simulate(0.1, 10);
        assertEquals(11, results.size()); // day 0 to 10
    }

    @Test
    void testDaysToTarget() {
        NetworkSimulationService sim = new NetworkSimulationService();
        int days = sim.daysToTarget(0.1, 500);
        assertTrue(days > 0 && days < 1000);
    }

    @Test
    void testMinBonusForTarget() {
        NetworkSimulationService service = new NetworkSimulationService();

        // Option 2: Stronger probability growth
        Function<Double, Double> adoptionProb = bonus -> Math.min(0.0002 * bonus, 1.0);

        int days = 30;
        int targetHires = 400;

        Integer bonus = service.minBonusForTarget(days, targetHires, adoptionProb, 1e-3);

        System.out.println("Returned bonus = $" + bonus);
        assertNotNull(bonus);
//        assertTrue(bonus % 10 == 0);
    }




}
