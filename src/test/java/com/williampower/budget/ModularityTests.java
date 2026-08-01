package com.williampower.budget;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

class ModularityTests {

	ApplicationModules modules = ApplicationModules.of(BudgetApplication.class);

	@Test
	void verifiesModularStructure() {
		modules.verify();
	}

	@Test
	void printsModuleStructure() {
		modules.forEach(System.out::println);
	}

}
