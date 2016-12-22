/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javarealization;

/** Интерфейс структур
 * 
 * Все структуры должны поддерживать текущий интерфейс.
 * Методы, присутствующие в интерфейсе, но неподдерживаемые определенной 
 * сруктурой должны быть заглушены.
 *
 * @author Akropon
 */
public interface Struct {
    
    /** Метод возвращения типа структуры
     * 
     * @return ответ метода
     */
    public String get_struct_type();
    
    /** Метод поиска
     * 
     * @param x
     * @return ответ метода
     */
    public String find(int x);
    
    /** Метод добавления
     * 
     * @param x
     * @param y
     */
    public void add(int x, int y);
    
    /** Метод удаления
     * 
     * @param x
     * @return ответ метода
     */
    public String delete(int x);
    
    /** Метод получения минимального элемента
     * 
     * @return ответ метода
     */
    public String get_min();
    
    /** Метод получения максимального элемента
     * 
     * @return ответ метода
     */
    public String get_max();
    
    /** Метод получения элемента по индексу
     * 
     * @return ответ метода
     */
    public String get_at(int x);
    
    /** Метод получения тестового изображения данных в структуре
     * 
     * @return ответ метода
     */
    public String get_data();
    
    /** Метод получения упрощенного тестового изображения данных в структуре
     * 
     * @return ответ метода
     */
    public String get_data_simple();
    
    /** Метод получения некоторых свойств и параметров структуры
     * 
     * @return ответ метода
     */
    public String get_state();
    
    /** Метод получения полной информации по структуре.
     * 
     * @return ответ метода
     */
    public String print();
}
