package org.dei.Sprint3.Services;

import oracle.jdbc.OracleTypes;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class GenerateRouteID {

    public static int run(Connection con) {
        int nextId = 1;
        // Chama a função SQL que criámos acima.
        // "? =" indica o retorno. Não há parâmetros de entrada "()".
        String sql = "{? = call getNextRouteId()}";

        try (CallableStatement stmt = con.prepareCall(sql)) {
            // Regista que o primeiro ? é um número de SAÍDA (Output)
            stmt.registerOutParameter(1, OracleTypes.NUMBER);

            // Executa
            stmt.execute();

            // Obtém o valor
            nextId = stmt.getInt(1);

        } catch (SQLException e) {
            System.err.println("[WARNING] DB ID Generation failed: " + e.getMessage());
            // Fallback temporário caso a BD falhe
            return (int) (System.currentTimeMillis() % 100000);
        }
        return nextId;
    }
}