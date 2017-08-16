import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
	
	private static int[][] entrada;
	private static int[] avaliacao;
	private static int[][] selecionados;
	
	public static void main(String[] args) {
		entrada = new int[20][8];
		avaliacao = new  int[20];
		selecionados = new  int[20][1];//i=solucao j=coluna k=avaliacao

		//ALGORITMO GENETICO		
		int T = 0;
		entrada = inicializaPopulacao(entrada);
		for(T=0;T<=100;T++){
			avaliacao = avaliaPopulacao(entrada);
			selecionados = selecionaPais(avaliacao,entrada);
//			recombinar(selecionados);
//			avaliacao = avaliaPopulacao(selecionados);
//			selecionados = selecionaPais(avaliacao,selecionados);
		}
		
	}
	/**
	 * SELECIONAR PAIS:
	 *   ORDENANDO VETOR DE AVALIAÇÃO
	 */
	private static int[][] selecionaPais(int[] avaliacao2, int[][] entrada2) {
		int[][] selecionados = new int[20][1];
		for (int i = 0; i < selecionados.length; i++) {
			selecionados[i] = entrada2[i];
			for (int j = 0; j < selecionados[0].length; j++) {
				selecionados[i][j] = avaliacao2[j];
			}
		}
		/*
		Arrays.sort(selecionados[i], new Comparator<int[]>() {
			@Override
			public int compare(int[] entry1, int[] entry2) {
				final Integer time1 = entry1[1];
				final Integer time2 = entry2[1];
				return time1.compareTo(time2);
			}
		});
		*/

		return selecionados;
	}

	private static int[] avaliaPopulacao(int[][] entrada2) {
		int[] avaliacao2 = new int[avaliacao.length];
		int[] teste = {1,4,6,8,5,2,7,3};
		entrada2[0]=teste;
		int maior = 0;
		int menor = 0;
		int principal = 0;
		for (int i = 0; i < entrada2.length; i++) {
			for (int j = 0; j < entrada2[0].length; j++) {
				if(entrada2[i][j] == j+1){
					principal++;
					System.out.println("Diagonal principal");
				}
				continua:
				for (int k = j+1; k < entrada2[0].length; k++) {
					if(k > entrada2[0].length){
						continue continua;
					}
					if(j-k == 1 || k-j == 1)
					if(entrada2[i][j]-entrada2[i][k]==1 || entrada2[i][j]-entrada2[i][k]==-1){
						avaliacao2[i]+=1;
						System.out.println("entrada2["+i+"]["+j+"] conflitou com entrada["+i+"]["+k+"]");
					}
					if(entrada2[i][j]>=entrada2[i][k]){
						maior = j+1;
						menor = k+1;
						if(maior-menor == entrada2[i][j]){
							avaliacao2[i]+=1;
							System.out.println("entrada2["+i+"]["+j+"] conflitou com entrada["+i+"]["+k+"]");
						}
					}else{
						maior = k+1;
						menor = j+1;
						if(maior-menor == entrada2[i][k]){
							avaliacao2[i]+=1;
							System.out.println("entrada2["+i+"]["+j+"] conflitou com entrada["+i+"]["+k+"]");
						}
					}
				}
			}
			if(principal>1){
				avaliacao2[i]+=principal;
				principal=0;
			}
		}
		return avaliacao2;
	}

	private static int[][] inicializaPopulacao(int[][] entrada2) {
		int[] modelo = { 1, 2, 3, 4, 5, 6, 7, 8 };
		for (int i = 0; i < entrada2.length; i++) {
			shuffleArray(modelo);
			for (int j = 0; j < modelo.length; j++) {
				entrada2[i][j] = modelo[j];
			}
		}
		//embaralha duplicados
		int[][] compara = entrada2;
		for (int i = 0; i < entrada2.length; i++) {
			for (int j = 0; j < entrada2.length; j++) {
				if (i != j && entrada2[i] == compara[j]) {
					shuffleArray(modelo);
					entrada2[j] = modelo;
				}
			}
		}
		for (int i = 0; i < entrada2.length; i++) {
			for (int j = 0; j < entrada2[0].length; j++) {
				if(j!=0){
					System.out.print("|"+entrada2[i][j]);
				}else{
					System.out.print("entrada2["+i+"]= "+entrada2[i][j]);
				}
			}
			System.out.println();
		}
		return entrada2;
	}

	private static int[] shuffleArray(int[] modelo) {
		Random rnd = ThreadLocalRandom.current();
	    for (int i = modelo.length - 1; i > 0; i--){
	      int index = rnd.nextInt(i + 1);
	      //swap
	      int a = modelo[index];
	      modelo[index] = modelo[i];
	      modelo[i] = a;
	    }
	    return modelo;
	}

	
}
