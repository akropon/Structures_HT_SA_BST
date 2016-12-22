package javarealization;

/** Отсортированный массив.
 * 
 * Структура, содержащая в себе элементы с ключами и значениями типа int.
 * Реализация отсортированного массива по неубыванию ключей.
 * Поддерживает возможность динамического расширения при переполнении.
 * Поддерживает возможность повтора элементов (т.е. в структуре могут 
 * одновременно присутствовать элементы с совпадающими значениями и/или ключами).
 * Присутствует внутреклассовая реализация элемента массива "Element".
 *
 * @author Akropon
 */
public class SortedArray implements Struct{
    protected Element [] data;              // массив с элементами
    protected int size;                 // текущее кол-во элементов 
    protected int max_available_size;   // текущий размер массива ´data´
    protected float extention_multiplier; // множитель динамического расширения массива
    
    // перенос строк. Так как он зависит от системы, определяем его во время запуска.
    protected static final String NL = System.getProperty("line.separator");
    
    /** Конструктор нового Отсортированного Массива
     * 
     * Создает новый пустой массив с доступной начальной памятью
     * под заданное кол-во элементов.
     * 
     * @param max_available_size > 0 - максимальное кол-во элементов в массиве до 
     * первого расширения
     * @param extention_multiplier > 1 (целочисленный) - множитель динамического расширения (при 
     * переполнении массива data[] выделяется новая память под этот массив, с 
     * размером в 'extention_multiplier' раз больше предыдущего).
     */
    public SortedArray(int max_available_size, float extention_multiplier){
        if ( max_available_size < 0 ) max_available_size = 1;
        if ( extention_multiplier < 1 ) extention_multiplier = 1.25f;
        
        
        this.max_available_size = max_available_size;
        this.data = new Element[max_available_size];
        this.size = 0;
        this.extention_multiplier = extention_multiplier;
    }
    
    /** Возвращает элемент по заданному индексу
     * 
     * @param index - индекс
     * @return "FAIL" - если был введен недопустимый индекс
     *         "key: X, value: Y", где X - ключ элемента, Y - соотв. значение
     */
    @Override
    public String get_at(int index) {
        if ( index < 0 || index >= size ) return "FAIL";
        return "key: "+data[index].key+", value: "+data[index].value;
    }
    
    
    /** Добавляет элемент в Осортированный массив.
     * 
     * Добавляет элемент с заданными ключем и значением.
     * В случае переполнения, расширяет массив с параллельными сортировкой и
     * вставкой нового элемента.
     * 
     * @param key - ключ
     * @param value - значение
     */
    @Override
    public void add(int key, int value) {
        // Найдем индекс позиции для нового элемента
        int left_index = 0;
        int right_index = size;
        while ( left_index < right_index ) {
          if ( key<data[(right_index+left_index)/2].key) 
            right_index = (right_index+left_index)/2;  // ищем в левой половине
          else 
            left_index = (right_index+left_index)/2+1;  // ищем в правой половине
        }
        // left_index – индекс позиции для нового элемента
        // Теперь проверим массив на переполнение
        if ( size >= max_available_size) {
            // Переполнение есть. Выделяем место под расширенный массив.
            // Копируем данные из старого массива в новый с учетом добавления
            // нового элемента.
            Element[] old_data = data;
            int new_size = Math.round(max_available_size * extention_multiplier);
            if (new_size <= max_available_size ) new_size = max_available_size+1;
            data = new Element[new_size];
            for ( int i=0; i<left_index; i++) 
                data[i] = old_data[i];
            data[left_index] = new Element(key,value);
            for ( int i=left_index; i<size; i++) 
                data[i+1] = old_data[i];
            // обновляем параметры структуры
            size++;
            max_available_size = new_size;
        } else {
            // Переполнения нет. Просто вставляем элемент.
            for ( int i=size; i>left_index; i--) 
                data[i] = data[i-1];
            data[left_index] = new Element(key, value);
            // обновляем параметры структуры
            size++;
        }
    }
    
    /** Удаляет элемент по заданному индексу.
     * 
     * @param index - индекс
     * @return "SUCCESS" - удален, "FAIL" - не удален
     */
    @Override
    public String delete(int index) {
        if (index < 0 || index >= size) return "FAIL";
        for ( int i=index; i<size-1; i++) {
            data[i] = data[i+1];
        }
        size--;
        return "SUCCESS";
    }
    
    /** Поиск элемента по значению
     * 
     * Ищет первый элемент с указанным ключем по алгоритму
     * бинарного поиска и возвращает его индекс.
     * В случае, если такой элемент отсутствует, возвращает "NOT FOUND".
     * 
     * @param key - ключ
     * @return : "NOT_FOUND" - искомый элемент отсутствует
     *           "X", X - индекс первого найденного элемента с ключем key
     */
    @Override
    public String find(int key) {
        int left_index = 0;
        int right_index = size-1;
        while ( left_index <= right_index ) {
          if ( key==data[(right_index+left_index)/2].key ) 
            return String.valueOf((right_index+left_index)/2);
          if ( key< data[(right_index+left_index)/2].key) 
            right_index = (right_index+left_index)/2-1;  // ищем в левой половине
          else 
            left_index = (right_index+left_index)/2+1;  // ищем в правой половине
        }
        return  "NOT FOUND"; // элемент не найден
    }
    
    /** Получить элемент с максимальным ключем
     * 
     * @return "NOT EXISTS" - такого нет
     *         "key: X, value: Y", где X - макс. ключ, Y - соотв. значение
     */
    @Override
    public String get_max() {
        if ( size > 0 )
            return "key: "+data[size-1].key+", value: "+data[size-1].value;
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
        if ( size > 0 )
            return "key: "+data[0].key+", value: "+data[0].value;
        else 
            return "NOT EXISTS";
    }
    
    /** Получить текстовое изображение массива.
     * 
     * Возвращает строку, в которой записаны все элементы массива в порядке, 
     * в котором они хранятся в структуре.
     * 
     * @return строка содержимого массива
     */
    @Override
    public String get_data() {
        if ( size == 0 ) return "array is empty";
        StringBuilder stringB = new StringBuilder();
        for ( int i=0; i<size; i++) {
            stringB.append(data[i].key);
            stringB.append(' ');
        }
        return stringB.toString();
    }
    
    
    /** Получить упрощенное текстовое изображение массива.
     * 
     * аналог get_data()
     * 
     * @return get_data()
     */
    @Override
    public String get_data_simple() {
        return get_data();
    }
    
    
    /** Получить текущее состояние массива.
     * 
     * Возвращает строку, в которой записаны значения всех параметров структуры.
     * В строке не записано содержимое массива data[].
     * 
     * @return строка состояния структуры
     */
    @Override
    public String get_state() {
        return "size = "+size+"  max_available_size = "+max_available_size
                + "  extention_multiplier = "+extention_multiplier;
    }
    
    
    /** Получить тип структуры
     * 
     * @return тип структуры
     */
    @Override
    public String get_struct_type() {
        return "sorted array";
    }
    
    
    /** Метод получения полной информации по структуре.
    * 
    * Выводит тип структуры, параметры и содержимое структуры
    * в текстовом виде.
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
                get_data();
    }
    
    /** Элемент для отсортированного массива SortedArray
     * 
     * @author Akropon
     */
    public static class Element {
        public int key;  // ключ
        public int value;  // значение
        
        /** Конструктор
         * 
         * Создает узел со всеми нулевыми полями.
         */
        public Element() {
            key = 0;  // нулевой ключ
            value = 0;  // нулевое значение
        }
        
        /** Конструктор
         * 
         * Создает узел с заданными ключем, значением
         * 
         * @param key - ключ
         * @param value - значение
         */
        public Element(int key, int value) {
            this.key = key;  
            this.value = value;
        }
    }
}
