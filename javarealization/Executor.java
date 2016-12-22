package javarealization;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/** Выполнятор
 * 
 * Читает команды из входного файла.
 * Последовательно их выполняет.
 * Весь вывод записывает в выходной файл.
 * Входной файл может содержать пустые строки,
 * поддерживает комментирование посредством "//",
 * каждая новая команда должна начинаться с начала новой строки.
 *
 * @author Akropon
 */
public class Executor {
    protected Struct struct; // структура, над которой выполняются команды
    protected FileWriter out; // поток вывода - подключен к файлу
    protected BufferedReader in; // поток входа - подключен к файлу
    /** Таблица команд.
     * 
     * Ключ - имя команды
     * Значение - объект, реализующий выполнение соответствующей команды
     */
    protected Map<String,CommandContainer> command_map;
    
    // перенос строк. Так как он зависит от системы, определяем его во время запуска.
    protected static final String NL = System.getProperty("line.separator");
    
    // команды, которые должны присутствовать во входном файле
    protected static final String CMD_NEW = "new";
    protected static final String CMD_ADD = "add";
    protected static final String CMD_DEL = "del";
    protected static final String CMD_FIND = "find";
    protected static final String CMD_AT = "at";
    protected static final String CMD_MIN = "min";
    protected static final String CMD_MAX = "max";
    protected static final String CMD_DATA = "data";
    protected static final String CMD_DATASIMPLE = "datasimple";
    protected static final String CMD_STATE = "state";
    protected static final String CMD_PRINT = "print";
    protected static final String ARG_HT = "ht";
    protected static final String ARG_SA = "sa";
    protected static final String ARG_BST = "bst";
    
    /** Конструктор.
     * 
     * Текущей структурой устанавливает BinSearchTree
 создает и заполняет таблицу команд,
 все остальные поля заполняет нулевыми указателями.
     */
    public Executor () {
        struct = new BinSearchTree();
        out = null;
        in = null;
        
        command_map = new HashMap<>();
        command_map.put(CMD_NEW, (CommandContainer) (String [] words) -> cmd_new(words));
        command_map.put(CMD_ADD, (CommandContainer) (String [] words) -> cmd_add(words));
        command_map.put(CMD_DEL, (CommandContainer) (String [] words) -> cmd_del(words));
        command_map.put(CMD_FIND, (CommandContainer) (String [] words) -> cmd_find(words));
        command_map.put(CMD_AT, (CommandContainer) (String [] words) -> cmd_at(words));
        command_map.put(CMD_MIN, (CommandContainer) (String [] words) -> cmd_min(words));
        command_map.put(CMD_MAX, (CommandContainer) (String [] words) -> cmd_max(words));
        command_map.put(CMD_DATA, (CommandContainer) (String [] words) -> cmd_data(words));
        command_map.put(CMD_DATASIMPLE, (CommandContainer) (String [] words) -> cmd_datasimple(words));
        command_map.put(CMD_STATE, (CommandContainer) (String [] words) -> cmd_state(words));
        command_map.put(CMD_PRINT, (CommandContainer) (String [] words) -> cmd_print(words));
    }
     
    /** Запуск обработчика.
     * 
     * Запускает обработчик команд, которые будут читаться из входного
     * файла, а выход записываться в выходной файл. Имена файлов задаются
     * в аргументах.
     * 
     * @param input_file_path - путь к входному файлу
     * @param output_file_path - пусть к выходному файлу
     */
    public void exec(String input_file_path, String output_file_path) {
        
        if (!opening_files(input_file_path, output_file_path)) return;
        
        executing_commands();
        
        closing_files();
    }
    
    /** Открытие потоков ввода и вывода по заданным путям к файлам
     * 
     * @param input_file_path - путь к входному файлу
     * @param output_file_path - пусть к выходному файлу
     * @return true - успех, false - не удалось
     */
    protected boolean opening_files(String input_file_path, String output_file_path) {
        try {
            out = new FileWriter(output_file_path);
        } catch (IOException e) {
            System.out.println("Error opening output-file:\n"+e.toString()+"\nProgram is interrupted.");
            return false;
        }
        
        try {
            in = new BufferedReader(new FileReader(input_file_path));
        } catch(IOException e) {
            System.out.println("Error opening input-file:\n"+e.toString()+"\nProgram is interrupted.");
            return false; 
        }
        
        return true;
    }
    
    
    /** Закрытия входного и выходного потока.
     * 
     * @return true - успех, false - не удалось
     */
    protected boolean closing_files() {
        boolean success = true;      
        
        try {
            in.close();
        } catch (IOException e) {
            System.out.println("Error closing input-file:\n"+e.toString()+"\n");
            success = false;
        }
        
        try {
            out.close();
        } catch (IOException e) {
            System.out.println("Error closing output-file:\n"+e.toString()+"\n");
            success = false;
        }
        
        return success;
    }

    
    /** Чтение команд и их выполнение.
     * 
     * Читает команды по одной. Распознает.
     * Выполняет. Выводит вывод в выходной файл.
     * 
     * В случае неверной команды выводит соответстующее сообщение и 
     * продолжает выполнение следующих команд.
     * 
     * (Полный список команд можно узнать в README.TXT)
     * 
     * @return true - ошибок не возникло. false - выполнение
     * было прервано из-за критических ошибок.
     */
    protected boolean executing_commands() {
        int num_of_line = -1;
        String line;
        String[] words;
        int comment_start_index;
        String response;
        
        try {
            while ( (line = in.readLine()) != null){
                num_of_line++;
                out.write(NL+">>"+line+NL);
                if ( (comment_start_index = line.indexOf("//")) != -1 )
                    line = line.substring(0, comment_start_index);
                words = line.split(" ");
                if ( words.length == 0 ) 
                    continue;
                if ( words[0].length() == 0) 
                    continue;
                
                
                try {
                    response = command_map.get(words[0]).exec_command(words);
                    out.write(NL+response+NL);
                } catch (Exception exc) {
                    out.write(NL+"ILLEGAL COMMAND"+NL);
                }
            }
            return true;
        } catch(Exception exc) {
            System.out.println("Error executing commands:"+NL+exc.toString()
                    +NL+"Aborting executing commands..."+NL);
            return false;
        }
    }
    
    /** Метод создания новой структуры
     * 
     * Используется в переопределении метода CommandContainer.exec() в 
     * таблице команд.
     * 
     * @param words - аргументы команды
     * @return "SUCCESS"
     * @throws Exception, если команда введена некорректно
     */
    protected String cmd_new(String [] words) throws Exception{
        if ( words[1].compareTo(ARG_HT) == 0)
            struct = new HTableChain(Integer.parseInt(words[2]));
        else if ( words[1].compareTo(ARG_BST) == 0 )
            struct = new BinSearchTree();
        else if ( words[1].compareTo(ARG_SA) == 0 ) {
            struct = new SortedArray(Integer.parseInt(words[2]), 
                                     Float.parseFloat(words[3]));
        }
        else throw new Exception();
        return "SUCCESS";
    }
    
    
    /** Метод добавления нового элемента
     * 
     * Используется в переопределении метода CommandContainer.exec() в 
     * таблице команд.
     * 
     * @param words - аргументы команды
     * @return "SUCCESS"
     * @throws Exception, если команда введена некорректно
     */
    protected String cmd_add(String [] words) throws Exception{
        struct.add(Integer.parseInt(words[1]), 
                   Integer.parseInt(words[2]));
        return "SUCCESS";
    }
    
    
    /** Метод удаления элемента команды
     * 
     * Используется в переопределении метода CommandContainer.exec() в 
     * таблице команд.
     * 
     * @param words - аргументы команды
     * @return Struct.delete(int x)
     * @throws Exception, если команда введена некорректно
     */
    protected String cmd_del(String [] words) throws Exception{
        return struct.delete(Integer.parseInt(words[1]));
    }
    
    
    /** Метод поиска элемента
     * 
     * Используется в переопределении метода CommandContainer.exec() в 
     * таблице команд.
     * 
     * @param words - аргументы команды
     * @return Struct.find(int x)
     * @throws Exception, если команда введена некорректно
     */
    protected String cmd_find(String [] words) throws Exception{
        return struct.find(Integer.parseInt(words[1]));
    }
    
    /** Метод получения элемента по индексу
     * 
     * Используется в переопределении метода CommandContainer.exec() в 
     * таблице команд.
     * 
     * @param words - аргументы команды
     * @return Struct.get_at(int x)
     * @throws Exception, если команда введена некорректно
     */
    protected String cmd_at(String [] words) throws Exception{
        return struct.get_at(Integer.parseInt(words[1]));
    }
    
    /** Метод получения минимального элемента
     * 
     * Используется в переопределении метода CommandContainer.exec() в 
     * таблице команд.
     * 
     * @param words - аргументы команды
     * @return Struct.get_min()
     * @throws Exception, если команда введена некорректно
     */
    protected String cmd_min(String [] words) throws Exception{
        return struct.get_min();
    }
    
    
    /** Метод получения максимального элемента 
     * 
     * Используется в переопределении метода CommandContainer.exec() в 
     * таблице команд.
     * 
     * @param words - аргументы команды
     * @return Struct.get_max()
     * @throws Exception, если команда введена некорректно
     */
    protected String cmd_max(String [] words) throws Exception{
        return struct.get_max();
    }
    
    
    /** Метод получения текствого изображения даннхых структуры
     * 
     * Используется в переопределении метода CommandContainer.exec() в 
     * таблице команд.
     * 
     * @param words - аргументы команды
     * @return Struct.get_data()
     * @throws Exception, если команда введена некорректно
     */
    protected String cmd_data(String [] words) throws Exception{
        return struct.get_data();
    }
    
    
    /** Метод получения упрощенного текствого изображения даннхых структуры
     * 
     * Используется в переопределении метода CommandContainer.exec() в 
     * таблице команд.
     * 
     * @param words - аргументы команды
     * @return Struct.get_datasimple()
     * @throws Exception, если команда введена некорректно
     */
    protected String cmd_datasimple(String [] words) throws Exception{
        return struct.get_data_simple();
    }
    
    
    /** Метод получения значений некоторых свойств и параметров структуры
     * 
     * Используется в переопределении метода CommandContainer.exec() в 
     * таблице команд.
     * 
     * @param words - аргументы команды
     * @return Struct.get_state()
     * @throws Exception, если команда введена некорректно
     */
    protected String cmd_state(String [] words) throws Exception{
        return struct.get_state();
    }
    
    
    /** Метод получения полной информации по структуре
     * 
     * Используется в переопределении метода CommandContainer.exec() в 
     * таблице команд.
     * 
     * @param words - аргументы команды
     * @return Struct.print()
     * @throws Exception, если команда введена некорректно
     */
    protected String cmd_print(String [] words) throws Exception{
        return struct.print();
    }
    
    
    /** Интерфейс исполнения команды
     * 
     * Позволяет реализовать объект, реализующий выполнение определенной команды,
     * принимающей на вход слова-аргументы, где:
     *   слово-аргумент 0 - сама команда
     *   слово-аргумент 1,2,... - соответственно аргументы для исполняемой команды
     * 
     * @author Akropon
     */
    protected interface CommandContainer {
        /** Метод, исполняющий реализованную в объекте команду
         * 
         * @param words - слова-аргументы
         * @return ответ выполнения команды
         * @throws java.lang.Exception
         */
        public String exec_command (String [] words) throws Exception;
    }
}
