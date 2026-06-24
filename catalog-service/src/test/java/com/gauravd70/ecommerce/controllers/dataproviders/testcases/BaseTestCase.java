package com.gauravd70.ecommerce.controllers.dataproviders.testcases;

import org.instancio.Instancio;

public class BaseTestCase {
    protected int getRandomInt(int start, int end) {
        return Instancio.gen().ints().range(start, end).get();
    }
}
