import java.util.ArrayList;
import java.util.List;

public class GCDemo {

    public static void main(String[] args) throws Exception {

        System.out.println("Iniciando DEMO DE GC...");

        aquecer(); // BLOCO 1

        System.out.println("\n--- FASE A: LIXO RÁPIDO (Young Gen) ---");
        gerarLixoRapido(); // BLOCO 2

        System.out.println("\n--- FASE B: OBJETOS SOBREVIVENTES (promoção para Old Gen) ---");
        gerarObjetosQueSobrevivem(); // BLOCO 3

        System.out.println("\n--- FASE C: FORÇAR OLD/MAJOR GC ---");
        forcarMajorGC(); // BLOCO 4

        System.out.println("\nFIM DO PROGRAMA.");
    }

    // BLOCO 1 — AQUECER CPU/JVM
    private static void aquecer() {
        System.out.println("Warmup iniciando...");

        for (int i = 0; i < 5_000_000; i++) {
            double x = Math.sqrt(i) * 0.0001;
        }

        System.out.println("Warmup finalizado!");
    }

    // BLOCO 2 — MINOR GC
    private static void gerarLixoRapido() {

        int repeticoes = 5_000_000;

        for (int i = 0; i < repeticoes; i++) {
            byte[] lixo = new byte[1024]; // 1 KB cada
        }

        System.out.println("Fase A: finalizada (lixo rápido gerado).");
    }

    // Lista que impede coleta (Old Gen)
    private static final List<byte[]> sobreviventes = new ArrayList<>();

    // BLOCO 3 — PROMOÇÃO PARA OLD GEN
    private static void gerarObjetosQueSobrevivem() {

        int repeticoes = 20_000;

        for (int i = 0; i < repeticoes; i++) {

            byte[] obj = new byte[10_000]; // 10 KB cada

            sobreviventes.add(obj); // impede GC de coletar

            if (i % 5_000 == 0) {
                System.out.println("Fase B: sobreviventes criados = " + i);
            }
        }

        System.out.println("Fase B: finalizada (objetos sobreviventes gerados).");
    }

    // BLOCO 4 — MAJOR / FULL GC
    private static void forcarMajorGC() {

        System.out.println("Fase C: iniciando pressão no Old Gen...");

        final int TAMANHO = 5 * 1024 * 1024; // 5 MB

        for (int i = 0; i < 30; i++) {

            byte[] lixoGrande = new byte[TAMANHO]; // 5MB

            System.out.println("Fase C: criado bloco grande (" + (i + 1) + "/30)");

            try {
                Thread.sleep(200); // tempo p/ GC agir
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("Fase C: finalizada.");
    }
}
