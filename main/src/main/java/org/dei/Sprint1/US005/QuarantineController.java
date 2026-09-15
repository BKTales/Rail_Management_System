package org.dei.Sprint1.US005;

import org.dei.Sprint1.Repository.TerminalRepository;
import org.dei._Facilities.Terminal.Terminal;
import org.dei._Facilities.Terminal.Warehouse.Warehouse;
import org.dei.Sprint1.US002.NotInitializedException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controller for the Returns & Quarantine process (USEI05).
 *
 * <p>This controller manages the quarantine inspection workflow for returned products.
 * When goods are returned to the warehouse, they are first quarantined to ensure that
 * damaged or expired items are not reintroduced into inventory. The inspection follows
 * a <b>LIFO (latest-first)</b> order, ensuring that the most recent returns are processed
 * first.</p>
 *
 * <p>During the inspection, each item is either:
 * <ul>
 *   <li><b>Restocked</b> — if the item is in good condition, it is reinserted into the
 *   warehouse inventory according to FEFO/FIFO rules (as defined in USEI01).</li>
 *   <li><b>Discarded</b> — if the item is damaged or expired, it is marked as unusable and
 *   excluded from inventory.</li>
 * </ul>
 *
 * <p>All inspection actions are recorded into a timestamped log file stored in the
 * <code>logs/</code> directory, providing a traceable audit history for accountability.</p>
 *
 * <p>Example of a generated log line:</p>
 * <pre>
 * 2025-09-22 14:32 | returnId=R105 | sku=SKU123 | action=Restocked | qty=8
 * 2025-09-22 14:40 | returnId=R106 | sku=SKU200 | action=Discarded
 * </pre>
 *
 */
public class QuarantineController {

    private TerminalRepository terminalRepo;
    private Terminal terminal;
    private List<Warehouse> warehouses;
    private static final String LOGS_DIR = "logs";

    /**
     * Constructs the controller and retrieves the singleton instance
     * of the {@link TerminalRepository}.
     */
    public QuarantineController() {
        terminalRepo = TerminalRepository.getInstance();
    }

    /**
     * Lists all available terminals that can perform quarantine operations.
     *
     * @return an array of terminal identifiers available in the system
     */
    public String[] listAvailableTerminals() {
        return terminalRepo.listTerminals();
    }

    /**
     * Retrieves a specific terminal from the repository by index.
     *
     * @param terminalIndex the index of the terminal selected by the user
     * @return the selected {@link Terminal} instance
     * @throws NotInitializedException if the terminal does not exist or is uninitialized
     */
    public Terminal getTerminal(int terminalIndex) throws NotInitializedException {
        terminal = terminalRepo.getTerminal(terminalIndex);
        if (terminal == null)
            throw new NotInitializedException("No Terminal Initialized!");

        return terminal;
    }

    /**
     * Executes the quarantine process for returned goods in the selected terminal.
     *
     * <p>This method performs the following actions:
     * <ul>
     *   <li>Invokes {@link Terminal#restockWarehouse()} to inspect and process all quarantined items.</li>
     *   <li>Generates a quarantine restock report summarizing all inspection actions.</li>
     *   <li>Writes the report to a timestamped log file under the <code>logs/</code> directory.</li>
     * </ul></p>
     *
     * @return a textual report summarizing the quarantine results
     */
    public String doQuarantine(){

        String result = terminal.restockWarehouse();

        writeToFile(result);

        return result;
    }

    /**
     * Ensures that the log directory exists; creates it if necessary.
     */
    private void createLogsDirectory() {
        File logsDir = new File(LOGS_DIR);
        if (!logsDir.exists()) {
            if (logsDir.mkdirs()) {
                System.out.println("Pasta 'logs' criada com sucesso");
            } else {
                System.err.println("Erro a criar pasta 'logs'");
            }
        }
    }

    /**
     * Writes the quarantine inspection results to a timestamped log file.
     *
     * <p>The file contains a report header, timestamp, inspection details,
     * and a footer marking the end of the report.</p>
     *
     * @param content the quarantine report to write to the file
     */
    private void writeToFile(String content) {
        createLogsDirectory();
        String fileName = generateFileName();
        String filePath = LOGS_DIR + File.separator + fileName;

        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
                System.out.println("Ficheiro criado: " + filePath);
            }

            try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
                writer.println("=== QUARANTINE RESTOCK REPORT ===");
                writer.println("Generated at: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                writer.println();
                writer.println(content);
                writer.println("=== END OF REPORT ===");
                writer.println();
            }

            System.out.println("Relatório guardado em: " + filePath);

        } catch (IOException e) {
            System.err.println("Erro a criar/escrever ficheiro: " + e.getMessage());
        }
    }

    /**
     * Generates a unique timestamped file name for the quarantine report.
     *
     * @return the generated file name (e.g. <code>quarantine_restock_20251025_153000.txt</code>)
     */
    private String generateFileName() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        return "quarantine_restock_" + timestamp + ".txt";
    }
}
