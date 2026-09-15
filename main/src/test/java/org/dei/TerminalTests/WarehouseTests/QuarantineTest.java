package org.dei.TerminalTests.WarehouseTests;

import org.dei.Sprint1._Item.*;
import org.dei.Sprint2.Country;
import org.dei._Location.GeographicalLocation;
import org.dei.Sprint2.TimeZone;
import org.dei.Sprint2.TimeZoneGroup;
import org.dei.Sprint1.Repository.TerminalRepository;
import org.dei._Facilities.Terminal.WarehouseServices.RestockService;
import org.dei._Facilities.Terminal.Terminal;
import org.dei._Facilities.Terminal.Warehouse.Aisle;
import org.dei._Facilities.Terminal.Warehouse.Bay;
import org.dei._Facilities.Terminal.Warehouse.Quarantine;
import org.dei._Facilities.Terminal.Warehouse.Warehouse;
import org.dei.Sprint1.US005.QuarantineController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class QuarantineTest {
    private Terminal terminal;
    private Warehouse warehouse;
    private Quarantine quarantine;
    private Item baseItem;
    private QuarantineController controller = new QuarantineController();


    @BeforeEach
    public void setUp() {
        warehouse = new Warehouse("1");
        quarantine = new Quarantine();
        baseItem = new Item(Unit.fromString("PACK"),"SKU123",1 , ItemType.fromString("ELECTRONICS"), 5.0);
    }

    private void addReturns(Quarantine quarantine) {
        quarantine.addItem(new QuarantineItem("RET00001", 19, baseItem, Reason.CYCLE_COUNT,
                null, Timestamp.valueOf("2025-09-13 12:00:00")));
        quarantine.addItem(new QuarantineItem("RET00004", 8, baseItem, Reason.EXPIRED,
                LocalDate.parse("2025-09-08"), Timestamp.valueOf("2025-09-08 08:00:00")));
        quarantine.addItem(new QuarantineItem("RET00005", 5, baseItem, Reason.DAMAGED,
                LocalDate.parse("2025-12-30"), Timestamp.valueOf("2025-09-14 09:00:00")));
        quarantine.addItem(new QuarantineItem("RET00006", 1, baseItem, Reason.WRONG_ITEM,
                LocalDate.parse("2025-11-11"), Timestamp.valueOf("2025-09-03 17:00:00")));
        quarantine.addItem(new QuarantineItem("RET00007", 11, baseItem, Reason.WRONG_ITEM,
                null, Timestamp.valueOf("2025-09-12 01:00:00")));
        quarantine.addItem(new QuarantineItem("RET00009", 19, baseItem, Reason.EXPIRED,
                LocalDate.parse("2026-03-22"), Timestamp.valueOf("2025-09-15 16:00:00")));
        quarantine.addItem(new QuarantineItem("RET00010", 1, baseItem, Reason.CYCLE_COUNT,
                null, Timestamp.valueOf("2025-09-15 21:00:00")));
        quarantine.addItem(new QuarantineItem("RET00013", 16, baseItem, Reason.CYCLE_COUNT,
                null, Timestamp.valueOf("2025-09-06 22:00:00")));
        quarantine.addItem(new QuarantineItem("RET00015", 8, baseItem, Reason.WRONG_ITEM,
                LocalDate.parse("2026-03-02"), Timestamp.valueOf("2025-09-11 20:00:00")));
        quarantine.addItem(new QuarantineItem("RET00018", 18, baseItem, Reason.DAMAGED,
                null, Timestamp.valueOf("2025-09-04 09:00:00")));
    }

    private void setupWarehouseStorage(Warehouse warehouse) {
        Aisle aisle = new Aisle();

        for (int i = 0; i < 3; i++) {
            Bay bay = new Bay(10);
            aisle.getBays().add(bay);
        }
        warehouse.getAisles().add(aisle);
    }

    @Test
    public void testQuarantineConstructorInitialState() {
        assertEquals(0, quarantine.getCurrentItems());
        assertEquals(50, quarantine.getMAX_ITEMS());
    }


    @Test
    public void testAddItemIncreasesSize() {
        QuarantineItem item = new QuarantineItem(
                "R1", 10, baseItem, Reason.CYCLE_COUNT,
                LocalDate.now().plusDays(30),
                Timestamp.valueOf(LocalDateTime.now())
        );

        quarantine.addItem(item);
        assertEquals(1, quarantine.getCurrentItems());
    }

    @Test
    public void testAddItemDoesNotExceedMaxLimit() {
        for (int i = 0; i < quarantine.getMAX_ITEMS() + 5; i++) {
            QuarantineItem item = new QuarantineItem(
                    "R" + i, 5, baseItem, Reason.CYCLE_COUNT,
                    LocalDate.now(),
                    Timestamp.valueOf(LocalDateTime.now().plusSeconds(i))
            );
            quarantine.addItem(item);
    }

    assertEquals(quarantine.getMAX_ITEMS(), quarantine.getCurrentItems());
    }

    @Test
    public void testPollReturnsMostRecentItem() {
        QuarantineItem older = new QuarantineItem(
            "OLD", 1, baseItem, Reason.CYCLE_COUNT,
            LocalDate.now(),
            Timestamp.valueOf(LocalDateTime.now().minusDays(1))
        );
        QuarantineItem newer = new QuarantineItem(
        "NEW", 1, baseItem, Reason.CYCLE_COUNT,
            LocalDate.now(),
            Timestamp.valueOf(LocalDateTime.now())
        );

        quarantine.addItem(older);
        quarantine.addItem(newer);

        QuarantineItem firstPolled = quarantine.poll();
        assertEquals("NEW", firstPolled.getId());
        assertEquals(1, quarantine.getCurrentItems());
    }

    @Test
    public void testPollReturnsNullWhenEmpty() {
        assertNull(quarantine.poll());
    }

    @Test
    void testRestockRunsAndProducesReport() {
        Terminal terminal = new Terminal(new GeographicalLocation(20.0,20.0), TimeZoneGroup.CET,new TimeZone("AE"),new Country("PT"),"STATION",10);
        warehouse = new Warehouse("W1");
        quarantine = new Quarantine();
        warehouse.setQuarantine(quarantine);
        terminal.addWarehouse(warehouse);
        setupWarehouseStorage(warehouse);
        addReturns(quarantine);

        String result = RestockService.restock(warehouse,0, new ArrayList<>());
        assertNotNull(result, "Restock result should not be null");
        assertTrue(result.contains("┌"), "Output should contain table header");
        assertTrue(result.contains("RESTOCKED"), "Should contain at least one restocked item");
        assertTrue(result.contains("DISCARDED"), "Should contain at least one discarded item");
        assertEquals(0, quarantine.getCurrentItems(), "After restock, quarantine should be empty");

        long restockedCount = result.lines().filter(line -> line.contains("RESTOCKED")).count();
        long discardedCount = result.lines().filter(line -> line.contains("DISCARDED")).count();

        assertTrue(restockedCount >= 5, "Expected at least 5 restocked items (non-damaged/non-expired)");
        assertTrue(discardedCount >= 2, "Expected at least 2 discarded items (damaged or expired)");

    }

    @Test
    public void testFileLogExist() {
        Terminal terminal = new Terminal(new GeographicalLocation(20.0,20.0),TimeZoneGroup.CET,new TimeZone("AE"),new Country("PT"),"STATION",10);
        TerminalRepository repo = TerminalRepository.getInstance();
        repo.add(terminal);
        controller.getTerminal(0);

        warehouse = new Warehouse("W1");
        quarantine = new Quarantine();
        warehouse.setQuarantine(quarantine);
        terminal.addWarehouse(warehouse);
        setupWarehouseStorage(warehouse);
        addReturns(quarantine);

        String result = controller.doQuarantine();
        assertNotNull(result, "Restock result should not be null");

        File logsDir = new File("logs/");
        assertTrue(logsDir.exists(), "Logs directory should exist");

        File[] logFiles = logsDir.listFiles((dir, name) ->
                name.startsWith("quarantine_restock_") && name.endsWith(".txt")
        );

        assertNotNull(logFiles, "Should be able to list log files");
        assertTrue(logFiles.length > 0, "At least one quarantine log file should have been created");

        assertTrue(doesLogFileContainContent(logFiles, result),
                "At least one log file should contain the expected content");

    }

    private boolean doesLogFileContainContent(File[] logFiles, String expectedContent) {
        if (logFiles == null) return false;

        for (File logFile : logFiles) {
            try {
                String content = new String(Files.readAllBytes(logFile.toPath()), StandardCharsets.UTF_8);
                if (content.contains(expectedContent)) {
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false; // no match found
    }
}