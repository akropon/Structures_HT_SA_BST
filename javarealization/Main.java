package javarealization;

/**
 *
 * @author Akropon
 */
public class Main {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
	// Создание обработчика.
        Executor ex = new Executor();
        
        // Имена файлов из аргументов командной строки
        String filename_in = "in.txt";
        String filename_out = "out.txt";
        if ( args.length >= 1 ) {
            filename_in = args[0];
            if ( args.length >= 2 ) {
                filename_out = args[1];
            }
        }
        
        // запуск обработчика
        ex.exec(filename_in, filename_out);
    }
}
