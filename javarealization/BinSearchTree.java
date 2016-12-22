package javarealization;

/** 
 * Двоичное дерево поиска.
 * 
 * Структура, содержащая в себе ключи и значения типа int.
 * Реализация двоичного дерева поиска по правилу ( левый сын меньше отца (по ключу) ).
 * Поддерживает возможность повтора элементов (т.е. в структуре могут 
 * одновременно присутствовать элементы с совпадающими ключами и/или значениями).
 * Присутствует внутреклассовая реализация узла дерева "Node".
 *
 * @author Akropon
 */
public class BinSearchTree implements Struct{
    protected Node root; // корень дерева
    
    // перенос строк. Так как он зависит от системы, определяем его во время запуска.
    protected static final String NL = System.getProperty("line.separator");
    
    /** Конструктор.
     * 
     * Создает дерево с корнем, указывающем на null
     */
    public BinSearchTree() {
        root = null;
    }
    
    /** Поиск по ключу.
     * 
     * @param key - ключ
     * @return "key: X, value: Y" - найден, 
     *         "NOT FOUND" - не найден
     */
    @Override
    public String find(int key) {
        if (root == null) {
            return "NOT FOUND";
        }
        
        Node node = root;
        while (true) {
            if ( key == node.key )
                return "key: "+node.key+", value: "+node.value;
            if ( key > node.key )
                if ( node.right == null )
                    return "NOT FOUND";
                else 
                    node = node.right;
            else // key < node.key
                if ( node.left == null )
                    return "NOT FOUND";
                else 
                    node = node.left;
        }
    }
    
    /** Добавление нового элемента с заданным ключем и значением
     * 
     * @param key - ключ
     * @param value - значение
     */
    @Override
    public void add(int key, int value) {
        if (root == null) {
            root = new Node(key, value);
            return;
        }
        
        Node node = root;
        while (true) {
            if ( key >= node.key ) {
                if ( node.right == null ) {
                    node.right = new Node(key, value, node);
                    return;
                }
                else 
                    node = node.right;
            } else { // key < node.key
                if ( node.left == null ) {
                    node.left = new Node(key, value, node);
                    return;
                }
                else 
                    node = node.left;
            }
        }
    }
    
    /** Удаление элемента по ключу
     * 
     * @param key - ключ
     * @return "SUCCESS" - удален, "FAIL" - не удален
     */
    @Override
    public String delete(int key) {
        if (root == null) {
            return "FAIL";
        }
        
        Node node = root;
        while (true) {
            if ( key == node.key ) {
                if ( node.left == null && node.right == null ) {
                    if ( node == root ) {
                        root = null;
                        return "SUCCESS";
                    }
                    // node != root
                    if ( node == node.father.right )
                        node.father.right = null;
                    else
                        node.father.left = null;
                    return "SUCCESS";
                }
                if ( node.left != null && node.right == null) {
                    if ( node == root ) {
                        root = node.left;
                        return "SUCCESS";
                    }
                    // node != root
                    if ( node == node.father.right ) {
                        node.father.right = node.left;
                        node.left.father = node.father;
                    }
                    else {
                        node.father.left = node.left;
                        node.left.father = node.father;
                    }
                    return "SUCCESS";
                }
                if ( node.left == null && node.right != null) {
                    if ( node == root ) {
                        root = node.right;
                        return "SUCCESS";
                    }
                    // node != root
                    if ( node == node.father.right ) {
                        node.father.right = node.right;
                        node.right.father = node.father;
                    }
                    else {
                        node.father.left = node.right;
                        node.right.father = node.father;
                    }
                    return "SUCCESS";
                }
                
                // node.left != null && node.right != null
                Node rstmn = node.right; // rstmn - RightSonTree-Min-Node
                while(rstmn.left != null) 
                    rstmn = rstmn.left;
                rstmn.father.right = rstmn.right;
                node.key = rstmn.key;
                node.value = rstmn.value;
                return "SUCCESS";
            }
            if ( key > node.key )
                if ( node.right == null )
                    return "FAIL";
                else 
                    node = node.right;
            else // key < node.key
                if ( node.left == null )
                    return "FAIL";
                else 
                    node = node.left;
        }
    }
    
    /** Получить элемент с минимальным ключем
     * 
     * @return "NOT EXISTS" - такого нет
     *         "key: X, value: Y", где X - мин. ключ, Y - соотв. значение
     */
    @Override
    public String get_min() {
        if ( root == null ) return "NOT EXISTS";
        Node node = root;
        while ( node.left != null )
            node = node.left;
        return "key: "+node.key+", value: "+node.value;
    }
    
    /** Получить элемент с максимальным ключем
     * 
     * @return "NOT EXISTS" - такого нет
     *         "key: X, value: Y", где X - макс. ключ, Y - соотв. значение
     */
    @Override
    public String get_max() {
        if ( root == null ) return "NOT EXISTS";
        Node node = root;
        while ( node.right != null )
            node = node.right;
        return "key: "+node.key+", value: "+node.value;
    }
    
    /** Получить глубину дерева
     * 
     * @return X >= 0 - глубина
     */
    int get_depth() {
        if ( root == null ) return 0;
        
        int max_depth = 0;
        int cur_depth = 0;
        Node cur_node = root;
        byte id_from = 0; // 0-пришел от отца, 1-пришел от левого сына, 2-от правого сына
        while(true) {
            switch ( id_from ) {
                case 0:
                    cur_depth++;
                    if (cur_depth > max_depth)
                        max_depth = cur_depth;
                    if (cur_node.left!=null) {
                        cur_node = cur_node.left;
                        //id_from=0;
                    }
                    else {
                        id_from = 1;
                        cur_depth++;
                    }
                    break;
                case 1:
                    cur_depth--;
                    if (cur_node.right!=null) {
                        cur_node = cur_node.right;
                        id_from=0;
                    }
                    else {
                        id_from = 2;
                        cur_depth++;
                    }
                    break;
                case 2:
                    cur_depth--;
                    if ( cur_node.father == null ) return max_depth;
                    if ( cur_node.father.left == cur_node )
                        id_from = 1;
                    else
                        id_from = 2;
                    cur_node = cur_node.father;
                    break;
                default: return -1; // ошибка
            }
        }
    }
    
    /** Получить текстовое изображение дерева
     * 
     * @return изображение
     */
    @Override
    public String get_data() {
        if ( root == null ) return "tree is empty";
        // поиск самого длинного ключа:
        int longest_key;
        Node node = root;
        while ( node.right != null )
            node = node.right;
        longest_key = Math.abs(node.key);
        node = root;
        while ( node.left != null )
            node = node.left;
        if ( Math.abs(node.key) > longest_key)
            longest_key = Math.abs(node.key);
        
        // размер поля под узел БЕЗ УЧЕТА ПРОБЕЛА, но с учетом знака "-"
        int item_size = (int)Math.floor(Math.log10(longest_key))+1+1; 
        // глубина дерева
        int depth = get_depth();
        // макс. возможное кол-во элементов на уровне
        int max_num_of_nodes_in_level = (int)Math.pow(2, depth-1);
        int max_string_length = max_num_of_nodes_in_level * (item_size+1);
        
        StringBuilder stringCommon = new StringBuilder();
        
        Node[] level_nodes = new Node[max_num_of_nodes_in_level];
        level_nodes[0] = root;
        int cur_level =  1;
        int nodes_at_cur_level = 1;
        // Рассмотрим каждый уровень
        while(cur_level <= depth) {
            StringBuilder stringFirst = new StringBuilder(); // Строка с элементом
            StringBuilder stringSecond = new StringBuilder(); // Строка с "палками"
            
            int cur_item_size = max_string_length/nodes_at_cur_level;
            // Рассмотрим участок строки для каждого элемента
            for (int i=0; i<nodes_at_cur_level; i++) { 
                // пустой узел?
                if (level_nodes[i] == null ) {
                    for ( int j=0; j<cur_item_size; j++) {
                        stringFirst.append(' ');
                        stringSecond.append(' ');
                    }
                } else { // Узел не пустой!
                    int key_of_node = level_nodes[i].key;
                    int key_str_size;
                    if (key_of_node == 0) 
                        key_str_size = 1;
                    else {
                        key_str_size = (int)Math.floor(Math.log10(Math.abs(key_of_node)))+1;
                        if (key_of_node<0) key_str_size++;
                    }
                
                    stringFirst.append(level_nodes[i].key);
                    if (cur_level != depth) {
                        if (level_nodes[i].left != null) 
                            stringSecond.append('|');
                        else 
                            stringSecond.append(' ');
                    }

                    if (cur_level != depth) 
                        if (level_nodes[i].right != null) {
                            for ( int j=key_str_size; j<cur_item_size/2; j++)
                                stringFirst.append('-');
                        } else {
                            for ( int j=key_str_size; j<cur_item_size/2; j++)
                                stringFirst.append(' ');
                        }
                    else 
                        for ( int j=key_str_size; j<cur_item_size; j++)
                            stringFirst.append(' ');
                    if (cur_level != depth) 
                        for ( int j=1; j<cur_item_size/2; j++)
                            stringSecond.append(' ');

                    if (cur_level != depth) {
                        if (level_nodes[i].right != null) 
                            stringFirst.append('-');
                        else
                            stringFirst.append(' ');
                    }
                    if (cur_level != depth) {
                        if (level_nodes[i].right != null)
                            stringSecond.append('|');
                        else
                            stringSecond.append(' ');
                    }

                    if (cur_level != depth) 
                        for ( int j=cur_item_size/2+1; j<cur_item_size; j++) {
                            stringFirst.append(' ');
                            stringSecond.append(' ');
                        }
                }
            }
            // Восстановим двойную строку для уровня
            stringCommon.append(stringFirst);
            stringCommon.append(NL);
            if (cur_level != depth) stringCommon.append(stringSecond);
            if (cur_level != depth) stringCommon.append(NL);
            
            // Есть ли след уровень?
            if ( cur_level == depth ) break; // нет. Хватит рисовать.
            // Переходим на след уровень
            cur_level++;
            nodes_at_cur_level *= 2;
            // Соберем данные по элементам для след уровня, используя пред
            for ( int i=nodes_at_cur_level/2-1; i>=0; i--){
                if ( level_nodes[i] != null ) {
                    level_nodes[2*i+1] = level_nodes[i].right;
                    level_nodes[2*i]   = level_nodes[i].left;
                }
                else {
                    level_nodes[2*i+1] = null;
                    level_nodes[2*i]   = null;
                }
            }
        }
        return stringCommon.toString();
    }
    
    /** Получить упрощенное текстовое изображение дерева
     * 
     * @return изображение
     */
    @Override
    public String get_data_simple() {
        StringBuilder stringB = new StringBuilder();
        if ( root == null ) return "tree is empty";
        
        // Выполним прямой обход дерева.
        Node cur_node = root;
        byte id_from = 0; // 0-пришел от отца, 1-пришел от левого сына, 2-от правого сына
        while(true) {
            switch ( id_from ) {
                case 0:
                    stringB.append(cur_node.key);
                    if (cur_node.left!=null) {
                        stringB.append("L");
                        cur_node = cur_node.left;
                        //id_from=0;
                    }
                    else {
                        id_from = 1;
                    }
                    break;
                case 1:
                    if (cur_node.right!=null) {
                        stringB.append("R");
                        cur_node = cur_node.right;
                        id_from=0;
                    }
                    else {
                        id_from = 2;
                    }
                    break;
                case 2:
                    if ( cur_node.father == null ) return stringB.toString();
                    if ( cur_node.father.left == cur_node )
                        id_from = 1;
                    else
                        id_from = 2;
                    stringB.append("P");
                    cur_node = cur_node.father;
                    break;
                default: return "error"; // ошибка(которой быть не должно)
            }
        }
    }
    
    /** Получить тип структуры
     * 
     * @return тип структуры
     */
    @Override
    public String get_struct_type() {
        return "binary search tree";
    }
    
    /** Заглушка для неподдерживаемого метода
     * 
     * @return сообщение об ошибке
     */
    @Override
    public String get_state() {
        return "Command is not supported for this structure";
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
    * Выводит тип структуры и содержимое структуры
    * в упрощенном текстовом виде.
    * 
    * @return ответ метода
    */
    @Override
    public String print() {
        return  "STRUCTURE TYPE:" + NL +
                get_struct_type() + NL +
                "DATA:" + NL +
                get_data_simple();
    }
    
    
    /** Узел для двоичного дерева поиска BinSearchTree
     * 
     * @author Akropon
     */
    public static class Node {
        public Node father; // Отец
        public Node left;  // Левый сын
        public Node right;  // Правый сын
        public int key;  // ключ
        public int value;  // значение
        
        /** Конструктор
         * 
         * Создает узел со всеми нулевыми полями.
         */
        public Node() {
            father = left = right = null; // нулевые указатели
            key = 0;  // нулевой ключ
            value = 0;  // нулевое значение
        }
        
        /** Конструктор
         * 
         * Создает узел с заданными ключем, значением и "нулевыми" указателями.
         * 
         * @param key - ключ
         * @param value - значение
         */
        public Node(int key, int value) {
            father = left = right = null;
            this.key = key;  
            this.value = value;
        }
        
        /** Конструктор
         * 
         * Создает узел с заданным ключем, ссылкой на отца и 
         * нулевыми ссылками на сыновей
         * 
         * @param key - ключ
         * @param value - значение
         * @param father - отец
         */
        public Node(int key, int value, Node father) {
            left = right = null;
            this.father = father;
            this.key = key;  
            this.value = value;
        }
    }
}