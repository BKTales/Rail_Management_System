package org.dei.Sprint1.US004;

import org.dei._Facilities.Terminal.Warehouse.PickingPath;
import org.dei._Facilities.Terminal.Warehouse.Trolley;

import java.util.List;
import java.util.Map;

public class PathPrinter {

    public static String printPaths(Map<Trolley, List<PickingPath>> paths) {
        StringBuilder sb = new StringBuilder();
        int index = 0;

        if (paths == null || paths.isEmpty()) {
            return "No paths available.\n";
        }
        sb.append("Paths were created:\n");

        for (Trolley t : paths.keySet()) {
            sb.append("╔══════════════════════════════════════════════════════════╗\n");
            sb.append(String.format("║  Trolley #0%-45s ║\n", index));
            sb.append("╠══════════════════════════════════════════════════════════╣\n");

            List<PickingPath> trolleyPickingPaths = paths.get(t);
            if (trolleyPickingPaths == null || trolleyPickingPaths.isEmpty()) {
                sb.append("║  No paths assigned.                                      ║\n");
                sb.append("╚══════════════════════════════════════════════════════════╝\n\n");
                index++;
                continue;
            }

            for (int i = 0; i < trolleyPickingPaths.size(); i++) {
                PickingPath p = trolleyPickingPaths.get(i);


                String strategy = (i == 0) ? "Strategy A (Ascending Aisle)"
                        : "Strategy B (Nearest Neighbour)";
                sb.append(String.format("║  %-55s ║\n", strategy));


                sb.append(p.toString());


                if (i < trolleyPickingPaths.size() - 1) {
                    sb.append("╠──────────────────────────────────────────────────────────╣\n");
                }
            }


            sb.append("╚══════════════════════════════════════════════════════════╝\n\n");
            index++;
        }

        return sb.toString();
    }
}
