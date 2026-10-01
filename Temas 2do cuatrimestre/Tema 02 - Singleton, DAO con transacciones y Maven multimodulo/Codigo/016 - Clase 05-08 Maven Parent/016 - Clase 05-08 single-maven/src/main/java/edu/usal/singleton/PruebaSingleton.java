package edu.usal.singleton;

public class PruebaSingleton {

    public static void main(String[] args) {
        ConfigSingleton config1 = ConfigSingleton.getInstance();
        ConfigSingleton config2 = ConfigSingleton.getInstance();

        if(config1 == config2){
            System.out.println("Instancias Iguales");
            System.out.println("Config1 " + config1);
            System.out.println("Config2 " + config2);
        } else {
            System.out.println("Instancias distintas");
        }
    }
}
