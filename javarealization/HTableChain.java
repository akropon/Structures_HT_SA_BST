package javarealization;

/** Структура - Хэш-таблица
 * 
 * Адресация - цепочная
 * Элементы содержат ключи и значения типа int
 * 
 * Содержит подкласс HChain, реализующий свойства звена цепочки.
 *
 * @author Akropon
 */
public class HTableChain implements Struct{
    
    protected HChain[] table; // массив начальных указателей на звенья цепочек
    protected int num_of_items; // кол-во звеньев в таблице
    protected int table_size; // размер массива начальных указателей на звенья цепочек
    
    // перенос строк. Так как он зависит от системы, определяем его во время запуска.
    protected static final String NL = System.getProperty("line.separator");
    
    /** Хэш-фукнция
     * 
     * Возвращает остаток от деления аргумента на размер массива начальных указателей
     *   по модулю этого размера.
     * 
     * @param argument - аргумент
     * @return - остаток
     */
    protected int hFunc(int argument) {
        int res = argument % table_size;
        return res < 0 ? res+table_size : res;
    }
    
    /** Конструктор
     * 
     * Создает пустую хэш-таблицу с заданной длиной массива начальных указателей
     * 
     * @param table_size - длина массива нач. указаелей
     */
    public HTableChain(int table_size) {
        if ( table_size < 1 ) table_size = 1;
        this.table_size = table_size;
        this.num_of_items = 0;
        this.table = new HChain[table_size];
        for (int i=0; i<table_size; i++)
            this.table[i] = null;
    }
    
    /** Добавление звена в хэш-таблицу по ключу
     * 
     * @param key - ключ
     * @param value - значение
     */
    @Override
    public void add(int key, int value) {
        int target_index = hFunc(key);
        HChain new_chain = new HChain(key, value, table[target_index]);
        table[target_index] = new_chain;
        num_of_items++;
    }
    
    /** Удаление звена из хэш-таблицы по ключу
     * 
     * @param key - ключ
     * @return "SUCCESS" - удален, "FAIL" - не удален
     */
    @Override
    public String delete(int key) {
        int target_index = hFunc(key);
        if (table[target_index] == null)  return "FAIL";
        if (table[target_index].key == key) {
            table[target_index] = table[target_index].next;
            num_of_items--;
            return "SUCCESS";
        }
        HChain cur_chain = table[target_index];
        while(true) {
            if (cur_chain.next == null)  return "FAIL";
            if (cur_chain.next.key == key) { 
                cur_chain.next = cur_chain.next.next;
                num_of_items--;
                return "SUCCESS";
            }
            cur_chain = cur_chain.next;
        }
    }
    
    /** Поиск элемента в хэш-таблице по ключу
     * 
     * @param key - ключ
     * @return "key: X, value: Y" - найден, 
     *         "NOT FOUND" - не найден
     */
    @Override
    public String find(int key) {
        HChain cur_chain = table[hFunc(key)];
        while(true) {
            if (cur_chain == null)  return "NOT FOUND";
            if (cur_chain.key == key)
                return "key: "+cur_chain.key+", value: "+cur_chain.value;
            cur_chain = cur_chain.next;
        }
    }
    
    /** Получить элемент с максимальным ключем
     * 
     * @return "NOT EXISTS" - такого нет
     *         "key: X, value: Y", где X - макс. ключ, Y - соотв. значение
     */
    @Override
    public String get_max() {
        HChain max_chain = null;
        HChain cur_chain;
        for ( int i=0; i<table_size; i++ ) {
            cur_chain = table[i];
            while ( cur_chain != null ) {
                if ( max_chain == null ) max_chain = cur_chain;
                else 
                    if (cur_chain.key > max_chain.key) 
                        max_chain = cur_chain;
                cur_chain = cur_chain.next;
            }
        }
        if (max_chain != null )
            return "key: "+max_chain.key+", value: "+max_chain.value;
        else
            return "NOT EXISTS";
    }
    
    /** Получить элемент с минимальным ключем
     * 
     * @return "NOT EXISTS" - такого нет
     *         "key: X, value: Y", где X - мин. ключ, Y - соотв. значение
     */
    @Override
    public String get_min() {
        HChain min_chain = null;
        HChain cur_chain;
        for ( int i=0; i<table_size; i++ ) {
            cur_chain = table[i];
            while ( cur_chain != null ) {
                if ( min_chain == null ) min_chain = cur_chain;
                else 
                    if (min_chain.key < min_chain.key) 
                        min_chain = cur_chain;
                cur_chain = cur_chain.next;
            }
        }
        if (min_chain != null )
            return "key: "+min_chain.key+", value: "+min_chain.value;
        else
            return "NOT EXISTS";
    }
    
    /** Получить тектовое изображение таблицы
     * 
     * @return изображение
     */
    @Override
    public String get_data() {
        StringBuilder stringB = new StringBuilder();
        HChain cur_chain;
        for ( int i=0; i<table_size; i++ ) {
            stringB.append("  [");
            stringB.append(i);
            stringB.append("] -> ");
            cur_chain = table[i];
            while ( true ) {
                if ( cur_chain == null ) {
                    stringB.append("NULL"+NL);
                    break;
                }
                else {
                    stringB.append(cur_chain.key);
                    stringB.append(" -> ");
                    cur_chain = cur_chain.next;
                }
            }
        }
        return stringB.toString();
    }
    
    /** Получить упрощенное тектовое изображение таблицы
     * 
     * @return изображение
     */
    @Override
    public String get_data_simple() {
        StringBuilder stringB = new StringBuilder();
        HChain cur_chain;
        for ( int i=0; i<table_size; i++ ) {
            stringB.append("[");
            stringB.append(i);
            stringB.append("]-");
            cur_chain = table[i];
            while ( true ) {
                if ( cur_chain == null ) {
                    stringB.append("N ");
                    break;
                }
                else {
                    stringB.append(cur_chain.key);
                    stringB.append("-");
                    cur_chain = cur_chain.next;
                }
            }
        }
        return stringB.toString();
    }
    
    /**Получить основные параметры хэш-таблицы без данных в виде строки
     * 
     * @return строка 
     */
    @Override
    public String get_state() {
        return "num_of_items = "+num_of_items+" || table_size = "+table_size;
    }
    
    
    /** Получить тип структуры
     * 
     * @return тип структуры
     */
    @Override
    public String get_struct_type() {
        return "hash-table";
    }
    
    /** Заглушка для неподдерживаемого метода
     * 
     * @return сообщение об ошибке
     */
    @Override
    public String get_at(int index) {
        return "Command is not supported for this structure";
    }
    
    /** Метод получения полной информации по структуре.
    * 
    * Выводит тип структуры, параметры и содержимое структуры
    * в упрощенном текстовом виде.
    * 
    * @return ответ метода
    */
    @Override
    public String print() {
        return  "STRUCTURE TYPE:" + NL +
                get_struct_type() + NL +
                "STATE:" + NL +
                get_state() + NL +
                "DATA:" + NL +
                get_data_simple();
    }
    
    
    /** Звено
     * 
     * Звено для цепочек в хэш-таблице с цепочной адресацией HTableChain
     * 
     * 
     * @author Akropon
     */
    public static class HChain {
        public int key; // Ключ
        public int value; // Значение
        public HChain next; // Указатель на след. звено
        
        /** Конструктор
         * 
         * Создает звено с ключем "0", значением "0" и нулевым указателем на след звено
         */
        public HChain() {
            value = 0;
            key = 0;
            next = null;
        }
        
        /** Конструктор 
         * 
         * Создает звено с заданными ключем, значением и нулевым указателем на след звено
         * 
         * @param key - ключ
         * @param value - значение
         */
        public HChain(int key, int value) {
            this.key = key;
            this.value = value;
            next = null;
        }
        
        /** Конструктор
         * 
         * Создает звено с заданными ключем и указателем на след звено
         * 
         * @param key - ключ
         * @param value - значение
         * @param next - указатель на след звено
         */
        public HChain(int key, int value, HChain next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
