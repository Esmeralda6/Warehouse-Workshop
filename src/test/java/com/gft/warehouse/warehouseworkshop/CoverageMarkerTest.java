package com.gft.warehouse.warehouseworkshop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

//Clase de Jacoco, genera reportes de coverage para la pipeline
class CoverageMarkerTest {
    @Test
    void coversAllMethods() {
        CoverageMarker marker = new CoverageMarker();
        Assertions.assertEquals(1, marker.value());
        Assertions.assertEquals("ok", marker.status());
    }
}