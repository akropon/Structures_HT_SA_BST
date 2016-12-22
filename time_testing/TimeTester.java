package time_testing;

import java.io.FileWriter;
import javarealization.HTableChain;
import javarealization.BinSearchTree;
import javarealization.SortedArray;
import javarealization.Struct;

/** Измерятор времени выполнения основных методов структур
 * 
 * Отчет почти по всем действиям выводится в файл timetest_report.txt
 *
 * @author Akropon
 */
public class TimeTester {
    
    // файл записи отчета
    static FileWriter out; // поток вывода - подключен к файлу
    
    // Точка запуска
    public static void main(String[] args) {
	try {
            open_reporting();
            
            // Добавить сюда все необходимые тайм-тесты
            timetest_add();
            
            close_reporting();
            System.out.println("PRORAM WAS CORRECTLY FINISHED");
        }
        catch(Exception exc) {
            System.out.println("\nEXCEPTION WAS CATCHED. ITS TEXT:\n");//+exc.toString());
            exc.printStackTrace(System.out);
        }
    }
    
    // Открытие файла вывода отчета
    static void open_reporting() throws Exception {
        out = new FileWriter("timetest_report.txt");
    }
    
    // Закрытие файла вывода отчета
    static void close_reporting() throws Exception {
        out.close();
    }
    
    // Замер времени на ADD
    static void timetest_add()  throws Exception {
        int N = 10000;
        
        
        
        out.write("\n\n");
        out.write("/===========================================/\n");
        out.write("/=============== TIMETEST_ADD ==============/\n");
        out.write("/===========================================/\n\n");
        
        Struct ht = new HTableChain(N);
        Struct bst = new BinSearchTree();
        Struct sa = new SortedArray(10,2);
        timetest_add_for_struct_sim(ht, "HASH-TABLE", N);
        timetest_add_for_struct_sim(bst, "BIN-SEARCH-TREE", N);
        timetest_add_for_struct_sim(sa, "SORTED-ARRAY", N);
    }
    static void timetest_add_for_struct(Struct struct, 
                                        String struct_name,
                                        int N)  throws Exception {
        long start_com;
        long []fin_decs_com = new long[10];
        long start_adds;
        long []fin_decs_adds = new long[10];
        int value;
        long []time_decs_adds = new long[10];
        long []time_decs_com = new long[10];
        long []time_decs_clear = new long[10];
        
        out.write("\n"+struct_name+"\n\n");
        
        start_adds = System.nanoTime();
        for ( int i = 0; i<10; i++ ) {
            for ( int j = 0; j<N; j++ ) {
                //value = N-(10*i+j);
                //value = (int)(N*Math.random());
                value = 1;
            }
            fin_decs_adds[i] = System.nanoTime();
        }
        
        start_com = System.nanoTime();
        for ( int i = 0; i<10; i++ ) {
            for ( int j = 0; j<N; j++ ) {
                //value = N-(10*i+j);
                //value = (int)(N*Math.random());
                value = 1;
                struct.add(value, value);
            }
            fin_decs_com[i] = System.nanoTime();
        }
        
        out.write("№_period  common_time  adds_time  clear_time  delta_clear_time\n");
        for (int i = 0; i<10; i++) {
            time_decs_adds[i] = fin_decs_adds[i] - start_adds;
            time_decs_com[i] = fin_decs_com[i] - start_com;
            time_decs_clear[i] = time_decs_com[i] - time_decs_adds[i];
            long delta_clear_time = i==0 ? 0 : time_decs_clear[i]-time_decs_clear[i-1];
            out.write(i+"  "+time_decs_com[i]+"  "
                    +time_decs_adds[i]+"  "+time_decs_clear[i]+"  "+delta_clear_time+"\n");
        }
    }
    static void timetest_add_for_struct_alt(Struct struct, 
                                            String struct_name,
                                            int N)  throws Exception {
        long start_com;
        long []fin_decs_com = new long[10];
        //long start_adds;
        //long []fin_decs_adds = new long[10];
        int value;
        //long []time_decs_adds = new long[10];
        //long []time_decs_com = new long[10];
        //long []time_decs_clear = new long[10];
        
        out.write("\n"+struct_name+"\n\n");
        
        start_com = System.nanoTime();
        for ( int i = 0; i<10; i++ ) {
            int j_max = (i+1)*N;
            start_com = System.nanoTime();
            for ( int j = 0; j<j_max; j++ ) {
                //value = (int)(N*Math.random());
                //value = 1;
                struct.add(j, j);
            }
            fin_decs_com[i] = System.nanoTime();
            fin_decs_com[i] -= start_com;
        }
        
        out.write("N  common_time\n");
        for (int i = 0; i<10; i++) {
            out.write((i+1)*N+"  "+fin_decs_com[i]+"\n");
        }
        
        
        
    }
    static void timetest_add_for_struct_sim(Struct struct, 
                                            String struct_name,
                                            int N)  throws Exception {
        long start_com;
        long []fin_decs_com = new long[10];
        long []time_decs_com = new long[10];
        int value;
        int memory;
        
        out.write("\n"+struct_name+"\n\n");
        
        start_com = System.nanoTime();
        for ( int i = 0; i<10; i++ ) {
            //memory = N*(10-i);                      // L
            //memory = N*i;                           // M
                                                    // R
            //value = 7;                             // C   
            for ( int j = 0; j<N; j++ ) {
                //                                   // C
                value = (int)(N*Math.random());    // R
                //value = memory+j;                  // M
                //value = memory-j;                  // L
                struct.add(value, value);
            }
            fin_decs_com[i] = System.nanoTime();
        }
        
        out.write("№_period    N    common_time    delta_com_time\n");
        for (int i = 0; i<10; i++) {
            time_decs_com[i] = fin_decs_com[i] - start_com;
            long delta_com_time = i==0 ? 0 : time_decs_com[i]-time_decs_com[i-1];
            out.write(i+"  N="+(i+1)*N+"  "+time_decs_com[i]/1000+"u "+delta_com_time/1000+"u\n");
        }
    }
    
    // Замер времени на DEL
    static void timetest_del()  throws Exception {
        int N = 1000;
        /** time_decs[x][y][z]
         *  x - struct: 0 - bst, 1 - sa, 2 - ht
         *  y - test:   0 - slow, 1 - fast, 2 - rand
         *  z - time_results: i - time for n=N*(i+1);
         * 
         */
        long[][][] time_decs = new long[3][3][10];
        Struct []structs = null;       // struct: 0 - bst, 1 - sa, 2 - ht
        int bst_extra_order[] = null;   // массив ключей в псевдо-порядкя для бинарного дерева поиска,
                                        // применяя удаление к которым, можно получить удаление
                                        // псевдо-прямого порядка и псевдо-обратного
        
        out.write("\n\n");
        out.write("/===========================================/\n");
        out.write("/============== TIMETEST_DELETE ============/\n");
        out.write("/===========================================/\n\n");
        
        for (int rep=0; rep<2; rep++) {
            for(int i=0; i<10; i++) {
                structs = new Struct[3];
                bst_extra_order = new int[N*(i+1)];
                reset_structs(structs, bst_extra_order, N, N*(i+1));
                for (int s=0; s<3; s++) 
                    time_decs[s][0][i] = timetest_del_back_test(structs[s], bst_extra_order, N*(i+1));
                reset_structs(structs, bst_extra_order, N, N*(i+1));
                for (int s=0; s<3; s++) 
                    time_decs[s][1][i] = timetest_del_forward_test(structs[s], bst_extra_order, N*(i+1));
                reset_structs(structs, bst_extra_order, N, N*(i+1));
                for (int s=0; s<3; s++) 
                    time_decs[s][2][i] = timetest_del_rand_test(structs[s], N*(i+1));
            }
        }
        
        /*structs = reset_structs(10, 40);
        for (int s=0; s<3; s++) 
                out.write("\n"+structs[s].get_data()+"\n");  // DEBUGGGGGGGGG  */
        
        out.write("\n== BACK TEST ==\n");
        out.write("\n  [i] [N] [result for BST] [result for SA] [result for HT]\n");
        for (int i=0; i<10; i++) {
            out.write("  "+(i+1)+"  "+N*(i+1)+"  ");
            for (int s=0; s<3; s++) 
                out.write(time_decs[s][0][i]/1000+"u  ");  // nano to micro seconds
            out.write("\n");
        }
        
        
        out.write("\n== FORWARD TEST ==\n");
        out.write("\n  [i] [N] [result for BST] [result for SA] [result for HT]\n");
        for (int i=0; i<10; i++) {
            out.write("  "+(i+1)+"  "+N*(i+1)+"  ");
            for (int s=0; s<3; s++) 
                out.write(time_decs[s][1][i]/1000+"u  ");  // nano to micro seconds
            out.write("\n");
        }
        
        
        out.write("\n== RAND TEST ==\n");
        out.write("\n  [i] [N] [result for BST] [result for SA] [result for HT]\n");
        for (int i=0; i<10; i++) {
            out.write("  "+(i+1)+"  "+N*(i+1)+"  ");
            for (int s=0; s<3; s++) 
                out.write(time_decs[s][2][i]/1000+"u  ");  // nano to micro seconds
            out.write("\n");
        }
        
    }
    static long timetest_del_back_test(Struct struct, int[] bst_extra_order, int N) {
        long start;
        long stop;
        if (struct.getClass() == BinSearchTree.class) {
            start = System.nanoTime();
            for (int i=0; i<N; i++) {
                struct.delete(bst_extra_order[i]);
            }
            stop = System.nanoTime();
        } else {
            start = System.nanoTime();
            for (int i=0; i<N; i++) {
                struct.delete(i);
            }
            stop = System.nanoTime();
        }
        return stop-start;
    }
    static long timetest_del_forward_test(Struct struct, int[] bst_extra_order, int N) {
        long start;
        long stop;
        if (struct.getClass() == BinSearchTree.class) {
            start = System.nanoTime();
            for (int i=N-1; i>=0; i--) {
                struct.delete(bst_extra_order[i]);
            }
            stop = System.nanoTime();
        } else {
            start = System.nanoTime();
            for (int i=N-1; i>=0; i--) {
                struct.delete(i);
            }
            stop = System.nanoTime();
        }
        return stop-start;
    }
    static long timetest_del_rand_test(Struct struct, int N) {
        int[] val_arr = get_mixed_array(N);
        long start = System.nanoTime();
        for (int i=0; i<N; i++) {
            struct.delete(val_arr[i]);
        }
        long stop = System.nanoTime();
        return stop-start;
    }
    static int[] get_mixed_array(int N) {
        int [] arr = new int[N];
        for (int i=0; i<N; i++)
            arr[i] = i;
        int target, memory;
        for (int i=0; i<N; i++) {
            target = (int)(N*Math.random()); 
            memory = arr[i];
            arr[i] = arr[target];
            arr[target] = memory;
        }
        return arr;
    }
    static void reset_structs(Struct[] structs, int[] bst_extra_order, int ht_size, int N) {
        int[] bst_extra_order_cursor = new int[1];  // курсор, нужен для постройки массива
        bst_extra_order_cursor[0] = 0;
        structs[0] = built_tree_from_arr(N, bst_extra_order, bst_extra_order_cursor);
        structs[1] = new SortedArray(N, 2);
        for (int i=0; i<N; i++) structs[1].add(i,i);
        structs[2] = new HTableChain(ht_size);
        for (int i=0; i<N; i++) structs[2].add(i,i);
    }
    static BinSearchTree built_tree_from_arr(int N, 
            int[] bst_extra_order, int[] bst_extra_order_cursor) { // N - size of array
        BinSearchTree bst = new BinSearchTree();
        int[] arr = new int[N];
        for (int i=0; i<N; i++)
            arr[i] = i;
        hang_arr(bst,arr,0,N-1, bst_extra_order, bst_extra_order_cursor);
        return bst;
    }
    static void hang_arr(BinSearchTree bst, int[] arr, int beg, int end, 
            int[] bst_extra_order, int[] bst_extra_order_cursor) {
        // 'beg' - start index of array 'arr', witch must be hanged on tree 'bst'
        // 'end' - last index ---||---||---
        int mid = (beg+end)/2;
        bst.add(arr[mid], arr[mid]);
        if ( bst_extra_order != null ) {
            bst_extra_order[bst_extra_order_cursor[0]] = arr[mid];
            bst_extra_order_cursor[0]++;
        }
        if ( mid > beg ) hang_arr(bst, arr, beg, mid-1, bst_extra_order, bst_extra_order_cursor);
        if ( mid < end ) hang_arr(bst, arr, mid+1, end, bst_extra_order, bst_extra_order_cursor);
    }
    
    // Замер времени на FIND
    static void timetest_find()  throws Exception {
        int N = 1000;
        /** time_decs[x][y]
         *  x - struct: 0 - bst, 1 - sa, 2 - ht
         *  y - time_results: i - time for n=N*(i+1);
         * 
         */
        long[][] time_decs = new long[3][10];
        Struct []structs = null;       // struct: 0 - bst, 1 - sa, 2 - ht
        
        out.write("\n\n");
        out.write("/===========================================/\n");
        out.write("/=============== TIMETEST_FIND =============/\n");
        out.write("/===========================================/\n\n");
        
        for (int rep=0; rep<2; rep++) {
            for(int i=0; i<10; i++) {
                structs = new Struct[3];
                reset_structs(structs, null, N, N*(i+1));
                for (int s=0; s<3; s++) 
                    time_decs[s][i] = timetest_find_rand_test(structs[s], N*(i+1));
            }
        }
        
        out.write("\n== RAND TEST ==\n");
        out.write("\n  [i] [N] [result for BST] [result for SA] [result for HT]\n");
        for (int i=0; i<10; i++) {
            out.write("  "+(i+1)+"  "+N*(i+1)+"  ");
            for (int s=0; s<3; s++) 
                out.write(time_decs[s][i]/1000+"u  ");  // nano to micro seconds
            out.write("\n");
        }
        
    }
    static long timetest_find_rand_test(Struct struct, int N) {
        int[] val_arr = get_mixed_array(N);
        long start = System.nanoTime();
        for (int i=0; i<N; i++) {
            struct.find(val_arr[i]);
        }
        long stop = System.nanoTime();
        return stop-start;
    }
    
    // Замер времени на MIN
    static void timetest_min()  throws Exception {
        int N = 1000;
        /** time_decs[x][y]
         *  x - struct: 0 - bst, 1 - sa, 2 - ht
         *  y - time_results: i - time for n=N*(i+1);
         * 
         */
        long[][] time_decs = new long[3][10];
        Struct []structs = null;       // struct: 0 - bst, 1 - sa, 2 - ht
        
        out.write("\n\n");
        out.write("/===========================================/\n");
        out.write("/=============== TIMETEST_MIN ==============/\n");
        out.write("/===========================================/\n\n");
        
        for (int rep=0; rep<10; rep++) {
            for(int i=9; i>=0; i--) {
                structs = new Struct[3];
                reset_structs(structs, null, N, N*(i+1));
                for (int s=0; s<3; s++) 
                    time_decs[s][i] = timetest_min_test(structs[s], N*(i+1));
            }
        }
        
        out.write("\n== TEST ==\n");
        out.write("\n  // [av_result] - average time per one get_min()\n");
        out.write("\n  [i] [N] [av_result for BST] [av_result for SA] [av_result for HT]\n");
        for (int i=0; i<10; i++) {
            out.write("  "+(i+1)+"  "+N*(i+1)+"  ");
            for (int s=0; s<3; s++) 
                out.write(time_decs[s][i]+"n  ");  // average time per one comand in nanos
            out.write("\n");
        }
        
    }
    static long timetest_min_test(Struct struct, int N) throws Exception {
        long[] times = new long[N];
        long average_time = 0;
        long start1;
        long stop1;
        long start2;
        long stop2;
        
        for ( int i=0; i<N; i++) {
            start1 = System.nanoTime();
            //struct.get_min();
            stop1 = System.nanoTime();

            start2 = System.nanoTime();
            struct.get_min();
            stop2 = System.nanoTime();
            
            times[i] = (stop2-start2)-(stop1-start1);
        }
        
        for ( int i=0; i<N; i++) {
            average_time += times[i];
        }
        
        average_time /= N;
        
        return average_time;
    }
    
    // Замер времени на MAX
    static void timetest_max()  throws Exception {
        int N = 1000;
        /** time_decs[x][y]
         *  x - struct: 0 - bst, 1 - sa, 2 - ht
         *  y - time_results: i - time for n=N*(i+1);
         * 
         */
        long[][] time_decs = new long[3][10];
        Struct []structs = null;       // struct: 0 - bst, 1 - sa, 2 - ht
        
        out.write("\n\n");
        out.write("/===========================================/\n");
        out.write("/=============== TIMETEST_MAX ==============/\n");
        out.write("/===========================================/\n\n");
        
        for (int rep=0; rep<10; rep++) {
            for(int i=9; i>=0; i--) {
                structs = new Struct[3];
                reset_structs(structs, null, N, N*(i+1));
                for (int s=0; s<3; s++) 
                    time_decs[s][i] = timetest_max_test(structs[s], N*(i+1));
            }
        }
        
        out.write("\n== TEST ==\n");
        out.write("\n  // [av_result] - average time per one get_min()\n");
        out.write("\n  [i] [N] [av_result for BST] [av_result for SA] [av_result for HT]\n");
        for (int i=0; i<10; i++) {
            out.write("  "+(i+1)+"  "+N*(i+1)+"  ");
            for (int s=0; s<3; s++) 
                out.write(time_decs[s][i]+"n  ");  // average time per one comand in nanos
            out.write("\n");
        }
        
    }
    static long timetest_max_test(Struct struct, int N) throws Exception {
        long[] times = new long[N];
        long average_time = 0;
        long start1;
        long stop1;
        long start2;
        long stop2;
        
        for ( int i=0; i<N; i++) {
            start1 = System.nanoTime();
            stop1 = System.nanoTime();

            start2 = System.nanoTime();
            struct.get_max();
            stop2 = System.nanoTime();
            
            times[i] = (stop2-start2)-(stop1-start1);
        }
        
        for ( int i=0; i<N; i++) {
            average_time += times[i];
        }
        
        average_time /= N;
        
        return average_time;
    }
    
    
    // TODO
    static void timetest_common()  throws Exception {
        int N = 1000;
        /** time_decs[x][y][z]
         *  x - struct: 0 - bst, 1 - sa, 2 - ht
         *  y - scen: 0 - dynamic, 1 - mmf, 2 - uniform
         *  z - time_results: i - time for n=N*(i+1);
         * 
         */
        long[][][] time_decs = new long[3][3][10];
        Struct []structs = new Struct[3];       // struct: 0 - bst, 1 - sa, 2 - ht
        
        for (int i=0; i<10; i++) {
            structs[0] = new BinSearchTree();
            structs[1] = new SortedArray(N, 2);
            structs[2] = new HTableChain(N);
            for (int s=0; s<3; s++)
                time_decs[s][0][i] = timetest_common_DYNAMIC(structs[s], (i+1)*N);
            structs[0] = new BinSearchTree();
            structs[1] = new SortedArray(N, 2);
            structs[2] = new HTableChain(N);
            for (int s=0; s<3; s++)
                time_decs[s][1][i] = timetest_common_MMF(structs[s], (i+1)*N);
            structs[0] = new BinSearchTree();
            structs[1] = new SortedArray(N, 2);
            structs[2] = new HTableChain(N);
            for (int s=0; s<3; s++)
                time_decs[s][2][i] = timetest_common_UNIFORM(structs[s], (i+1)*N);
            
            
        }
        
        
        out.write("\n\n");
        out.write("/===========================================/\n");
        out.write("/=============== TIMETEST_COMMON ==============/\n");
        out.write("/===========================================/\n\n");
        
        out.write("\n\n== TEST_DYNAMIC ==\n");
        out.write("\n  [i] [N] [result for BST] [result for SA] [result for HT]\n");
        for (int i=0; i<10; i++) {
            out.write("  "+(i+1)+"  "+N*(i+1)+"  ");
            for (int s=0; s<3; s++) 
                out.write(time_decs[s][0][i]/1000+"u  ");  // in microsecs
            out.write("\n");
        }
        out.write("\n\n== TEST_MMF ==\n");
        out.write("\n  [i] [N] [result for BST] [result for SA] [result for HT]\n");
        for (int i=0; i<10; i++) {
            out.write("  "+(i+1)+"  "+N*(i+1)+"  ");
            for (int s=0; s<3; s++) 
                out.write(time_decs[s][1][i]/1000+"u  ");  // in microsecs
            out.write("\n");
        }
        out.write("\n\n== TEST_UNIFORM ==\n");
        out.write("\n  [i] [N] [result for BST] [result for SA] [result for HT]\n");
        for (int i=0; i<10; i++) {
            out.write("  "+(i+1)+"  "+N*(i+1)+"  ");
            for (int s=0; s<3; s++) 
                out.write(time_decs[s][2][i]/1000+"u  ");  // in microsecs
            out.write("\n");
        }
        
    }
    
    static long timetest_common_DYNAMIC(Struct struct, int N) {
        long start;
        long stop;
        int M;
        int k;
        
        // 1)
        int elements[] = get_mixed_array(N);
        for (int i=0; i<N; i++) struct.add(elements[i], elements[i]);
        
        // 2)
        M = 10*N;
        start = System.nanoTime();
        for (int i=0; i<M; i++) {
            k = (int)(N*Math.random());
            struct.find(k);
            struct.delete(k);
            struct.add(k,k);
        }
        stop = System.nanoTime();
        return stop-start;
    }
    
    static long timetest_common_MMF(Struct struct, int N) {
        long start;
        long stop;
        int M;
        int k;
        
        // 1)
        int elements[] = get_mixed_array(N);
        for (int i=0; i<N; i++) struct.add(elements[i], elements[i]);
        
        // 2)
        M = 10*N;
        start = System.nanoTime();
        for (int i=0; i<M; i++) {
            k = (int)(N*Math.random());
            struct.get_min();
            struct.get_max();
            struct.find(k);
        }
        stop = System.nanoTime();
        return stop-start;
    }
    
    static long timetest_common_UNIFORM(Struct struct, int N) {
        long start;
        long stop;
        int M;
        int k;
        
        // 1)
        int elements[] = get_mixed_array(N);
        for (int i=0; i<N; i++) struct.add(elements[i], elements[i]);
        
        // 2)
        M = 10*N/5;
        start = System.nanoTime();
        for (int i=0; i<M; i++) {
            k = (int)(N*Math.random());
            struct.find(k);
            struct.delete(k);
            struct.add(k,k);
            struct.get_min();
            struct.get_max();
        }
        stop = System.nanoTime();
        return stop-start;
    }
}
