package labsemana9;

public class TiempoProcesamiento {
    public TiempoProcesamiento(){
    }

    public static int tiempo(double peso){
        if(peso<=2.0){
            return 1;
        }
        if(peso<=5.0){
            return 2;
        }
        return 3;
    }
}
