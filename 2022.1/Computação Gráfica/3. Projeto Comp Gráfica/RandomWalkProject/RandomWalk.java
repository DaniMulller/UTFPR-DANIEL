import image.Image;

import java.awt.*;
import java.util.Random;

public class RandomWalk {
    public static void main(String args[]) throws Exception {
        int MAX_IT = 1000000; //quantidade maxima de iteracoes - default: 120000
        int pixels = 1024; //default: 256
        int QTD = 1; //quantidade de imagens geradas - default: 1

        long start = System.currentTimeMillis(); //inicia o cronometro
        for (int i = 0; i < QTD; i++) {

            Image map = new Image(pixels, pixels, 3); //instanciando uma nova imagem
            map = RandomWalkFunction_yellow(map, MAX_IT, pixels);

            Image map2 = new Image(pixels, pixels, 3);
            map2 = map.clone();
            map2 = RandomWalkFunction_green(map2, MAX_IT, pixels);

            map2.antiAlias(); //aplicando AntiAlias "suavizando"
            map2.showImage(); //mostrando a imagem final
        }
        long elapsed = System.currentTimeMillis() - start; //finaliza o cronometro

        System.out.println("Executed in: " + elapsed + "ms"); //printa o tempo de execucao
    }
    static Image RandomWalkFunction_yellow(Image map,double MAX_IT,int pixels) throws Exception {
        Random r = new Random(); //geracao de numeros randomicos

        int posX = r.nextInt(pixels), posY = r.nextInt(pixels); //gerando numeros randomicos de 0 a 255
        int it = 0;
        while (it < MAX_IT) { //fazendo as iteracoes
            //verificando se o pixel saiu fora do limite da imagem, e se sim, reposicionando em outro local randomico da imagem
            while (posX < 0 || posX >= map.getWidth() || posY < 0 || posY >= map.getHeight()){
                posX = r.nextInt(pixels);
                posY = r.nextInt(pixels);
            }
            //setando o pixel escolhido como branco (faz parte do movimento)
            map.setPixel(posX, posY, Color.green);
            //alterando o x e y;
            int randomPos = r.nextInt(4); //numero randomico de 0 ate 3
            if (randomPos == 0) {//direita
                posX += 1;
            } else if (randomPos == 1) {//esquerda
                posX -= 1;
            } else if (randomPos == 2) {//cima
                posY -= 1;
            } else {//baixo
                posY += 1;
            }
            it++;
        }
        dilate_map(map,7);
        erode_map(map,7);
        return map;
    }

    static Image RandomWalkFunction_green(Image map,double MAX_IT,int pixels) throws Exception {
        Random r = new Random(); //geracao de numeros randomicos

        int posX = r.nextInt(pixels), posY = r.nextInt(pixels); //gerando numeros randomicos de 0 a 255
        int it = 0;
        while (it < (MAX_IT)){
            while (posX < 0 || posX >= map.getWidth() || posY < 0 || posY >= map.getHeight() ||
                    ((int)map.getPixel(posX, posY, 0) != 0 && (int)map.getPixel(posX, posY, 1) != 255 && (int)map.getPixel(posX, posY, 2) != 0)) {
                posX = r.nextInt(pixels);
                posY = r.nextInt(pixels);
            }

            map.setPixel(posX, posY, dark_green);
            //alterando o x e y;
            int randomPos = r.nextInt(4); //numero randomico de 0 ate 3
            if (randomPos == 0) {//direita
                posX += 1;
            } else if (randomPos == 1) {//esquerda
                posX -= 1;
            } else if (randomPos == 2) {//cima
                posY -= 1;
            } else {//baixo
                posY += 1;
            }
            it++;
        }
        dilate_map_green(map,1);
        erode_map_green(map,1);
        map.antiAlias();
        for(int j=0;j<pixels;j++){
            for(int y=0;y<pixels;y++){
                if((int) map.getPixel(j, y, 0) == 0 && (int) map.getPixel(j, y, 1) == 0 && (int) map.getPixel(j, y, 2) == 0) {
                    map.setPixel(j, y, dark_blue); //definindo a cor do background
                }
            }
        }
        return map;
    }


    static Image dilate_map(Image map,int dilatation_size) throws Exception {
        Image dilate = new Image(dilatation_size, dilatation_size, 3);
        for(int i=0;i<dilatation_size;i++){
            for(int y=0;y<dilatation_size;y++){
                dilate.setPixel(i, y, Color.green);
            }
        }
        map.dilate(dilate,1,false);
        return map;
    }

    static Image erode_map(Image map,int erosion_size) throws Exception {
        Image erosion = new Image(erosion_size, erosion_size, 3);
        for(int i=0;i<erosion_size;i++){
            for(int y=0;y<erosion_size;y++){
                erosion.setPixel(i, y, Color.green);
            }
        }
        map.dilate(erosion,1,false);
        return map;
    }

    static Image dilate_map_green(Image map,int dilatation_size) throws Exception {
        Image dilate = new Image(dilatation_size, dilatation_size, 3);
        for(int i=0;i<dilatation_size;i++){
            for(int y=0;y<dilatation_size;y++){
                dilate.setPixel(i, y, dark_green);
            }
        }
        map.dilate(dilate,2,true);
        return map;
    }

    static Image erode_map_green(Image map,int erosion_size) throws Exception {
        Image erosion = new Image(erosion_size, erosion_size, 3);
        for(int i=0;i<erosion_size;i++){
            for(int y=0;y<erosion_size;y++){
                erosion.setPixel(i, y, dark_green);
            }
        }
        map.dilate(erosion,1,false);
        return map;
    }

    public static final Color dark_green = new Color(0, 153, 0); //grass
    public static final Color DARK_GREEN = dark_green;
    public static final Color dark_blue = new Color(0, 0, 204); //deep ocean
    public static final Color DARK_BLUE = dark_blue;
}