import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Evandro G.
 *
 */
public class Main {

	public static void main(String[] args) {

		int[][] entrada = new int[20][8];
		int[] avaliacao = new int[entrada.length];
		int[][] selecionados = new int[entrada.length / 2][entrada[0].length];

		entrada = inicializaPopulacao(entrada);
		for (int T = 0; T < 1000; T++) {
			avaliacao = avaliaPopulacao(entrada); // conta conflito entre rainhas
			selecionados = selecionaPais(avaliacao, entrada); // ordena pela avaliação e descarta metade
			entrada = recombinar(selecionados); // gera filhos com a metade gerada
			entrada = aplicaMutacao(entrada);
			if(avaliacao[0]==0){
				System.out.println("Encontrada solução!");
				for (int i = 0; i < entrada[0].length; i++) {
					System.out.print(entrada[i]);
				}
				System.out.println();
			}
//			avaliacao = avaliaPopulacao(entrada); // conta conflito entre rainhas
//			entrada = selecionaProximaEpoca(avaliacao, entrada); // sera utilizado nos proximos metodos
			
			/*for (int i = 0; i < avaliacao.length; i++) {
				if(avaliacao[i]==0){
					System.out.print("Encontrado sequencia sem erros: "+entrada[i][0]);
					for (int j = 1; j < entrada[0].length; j++) {
						System.out.print(entrada[i][j]);
					}
				}
			}*/
		}

	}

	/**
	 * ESTE METODO FOI CRIADO PARA FUTURAS EDICOES
	 */
	private static int[][] selecionaProximaEpoca(int[] avaliacao2, int[][] entrada2) {
		boolean changed = true;
		int tempAva = 0;
		int[] tempEnt = null;
		while (changed) {
			changed = false;
			for (int i = 0; i < avaliacao2.length - 1; i++) {
				if (avaliacao2[i + 1] < avaliacao2[i]) {
					// swap
					// swap(avaliacao2[i+1],avaliacao2[i]);
					tempAva = avaliacao2[i];
					avaliacao2[i] = avaliacao2[i + 1];
					avaliacao2[i + 1] = tempAva;
					// swap(entrada2[i+1],entrada2[i]);
					tempEnt = entrada2[i];
					entrada2[i] = entrada2[i + 1];
					entrada2[i + 1] = tempEnt;
					changed = true;
				}
			}
		}

		return entrada2;
	}

	private static int[][] aplicaMutacao(int[][] entrada2) {
		Random rnd = ThreadLocalRandom.current();
		int indice=0,coluna1=0,coluna2=0;
		while (coluna1==coluna2) {
			indice = 0 + rnd.nextInt(entrada2.length);
			coluna1 = 0 + rnd.nextInt(entrada2[0].length);
			coluna2 = 0 + rnd.nextInt(entrada2[0].length);
		}
		//swap
		int temp = entrada2[indice][coluna1];
		entrada2[indice][coluna1] = entrada2[indice][coluna2];
		entrada2[indice][coluna2] = temp;
		
		return entrada2;
	}

	private static int[][] recombinar(int[][] selecionados2) {
		int[][] populacao = new int[selecionados2.length * 2][selecionados2[0].length];
		int[][] filhos = new int[selecionados2.length][selecionados2[0].length];
		ArrayList<Integer> listaJaCombinados = new ArrayList<>();
		ArrayList<int[]> listaSelecionados = new ArrayList<>();
		Random rnd = ThreadLocalRandom.current();
		int pontoDeCorte = 1 + rnd.nextInt(selecionados2[0].length - 1);
		System.out.println("Ponto de corte: "+pontoDeCorte);
		int indiceFilhos = 0;
		for (int i = 0; i < filhos.length / 2; i++) {
			int controle = 0;
			int[] filhoEscolhido = new int[2];
			while (controle < 2) {
				int escolhido = 0 + rnd.nextInt(selecionados2.length);
				if (listaJaCombinados.isEmpty() || (!listaJaCombinados.contains(escolhido))) {
					filhoEscolhido[controle] = escolhido;
					listaJaCombinados.add(escolhido);
					controle++;
				}
			}
			int[][] filhosGerados = cutAndCrossFill(selecionados2[filhoEscolhido[0]], selecionados2[filhoEscolhido[1]],	pontoDeCorte);
			filhos[indiceFilhos] = filhosGerados[0];
			filhos[indiceFilhos + 1] = filhosGerados[1];
			indiceFilhos += 2;
		}
		for (int i = 0; i < selecionados2.length; i++) {
			if(!listaSelecionados.contains(selecionados2[i])){
				populacao[i] = selecionados2[i].clone(); // pais
				listaSelecionados.add(selecionados2[i].clone());
			}
		}
		indiceFilhos = 0;
		for (int i = selecionados2.length; i < populacao.length; i++) {
			if(!listaSelecionados.contains(filhos[indiceFilhos])){
				populacao[i] = filhos[indiceFilhos]; // filhos
				listaSelecionados.add(filhos[indiceFilhos]);
			}
			indiceFilhos++;
		}

		return populacao;
	}

	private static int[][] cutAndCrossFill(int[] array1, int[] array2, int pontoDeCorte) {
		int[][] filhos = new int[2][array1.length];
		int[] array = null;
		int[] arrayOutro = null;
		for (int i = 0; i < 2; i++) {
			int c = pontoDeCorte;
			if (i == 0) {
				array = array1.clone();
				arrayOutro = array2.clone();
			} else {
				array = array2.clone();
				arrayOutro = array1.clone();
			}
			for (int j = 0; j < pontoDeCorte; j++) {
				filhos[i][j] = array[j];
			}
			for (int k = 0; k < arrayOutro.length; k++) {
				if (!contemNumeroNoArray(arrayOutro[k], filhos[i])) {
					filhos[i][c] = arrayOutro[k];
					c++;
				}
			}

		}
		return filhos;
	}

	private static boolean contemNumeroNoArray(int num, int[] array) {
		for (int j = 0; j < array.length; j++) {
			if (array[j] == num) {
				return true;
			}
		}
		return false;
	}

	private static int[][] selecionaPais(int[] avaliacao2, int[][] entrada2) {
		int[][] selecionados2 = new int[entrada2.length / 2][entrada2[0].length];
		boolean changed = true;
		int tempAva = 0;
		int[] tempEnt = null;
		while (changed) {
			changed = false;
			for (int i = 0; i < avaliacao2.length - 1; i++) {
				if (avaliacao2[i + 1] < avaliacao2[i]) {
					// swap
					// swap(avaliacao2[i+1],avaliacao2[i]);
					tempAva = avaliacao2[i];
					avaliacao2[i] = avaliacao2[i + 1];
					avaliacao2[i + 1] = tempAva;
					// swap(entrada2[i+1],entrada2[i]);
					tempEnt = entrada2[i];
					entrada2[i] = entrada2[i + 1];
					entrada2[i + 1] = tempEnt;
					changed = true;
				}
			}
		}
		for (int i = 0; i < selecionados2.length; i++) {
			selecionados2[i] = entrada2[i];
		}

		return selecionados2;
	}

	private static int[][] inicializaPopulacao(int[][] entrada2) {
		int[] modelo = { 1, 2, 3, 4, 5, 6, 7, 8 };
		for (int i = 0; i < entrada2.length; i++) {
			modelo = shuffleArray(modelo);
			for (int j = 0; j < modelo.length; j++) {
				entrada2[i][j] = modelo[j];
			}
		}
		// embaralha duplicados
		int[][] compara = entrada2.clone();
		for (int i = 0; i < entrada2.length; i++) {
			for (int j = 0; j < entrada2.length; j++) {
				if (i != j && entrada2[i] == compara[j]) {
					modelo = shuffleArray(modelo);
					entrada2[j] = modelo;
				}
			}
		}
		for (int i = 0; i < entrada2.length; i++) {
			for (int j = 0; j < entrada2[0].length; j++) {
				if (j != 0) {
//					System.out.print("|" + entrada2[i][j]);
				} else {
//					System.out.print("entrada2[" + i + "]= " + entrada2[i][j]);
				}
			}
//			System.out.println();
		}
		return entrada2;
	}

	private static int[] shuffleArray(int[] modelo) {
		Random rnd = ThreadLocalRandom.current();
		for (int i = modelo.length - 1; i > 0; i--) {
			int index = rnd.nextInt(i + 1);
			// swap
			int a = modelo[index];
			modelo[index] = modelo[i];
			modelo[i] = a;
		}
		return modelo;
	}
	
	private static int[] avaliaPopulacao(int[][] entrada2) {
		int[] avaliacao2 = new int[entrada2.length];
//		int[] teste = {1,6,3,5,4,7,2,8};
//		entrada2[0] = teste;
		int maiorVetor;
		int menorVetor;
		int maiorIndice;
		int menorIndice;
		for (int v = 0; v < entrada2.length; v++) {//cada entrada
			for (int i = 0; i < entrada2[0].length; i++) {//cada casa
				for (int j = 0; j < entrada2[0].length; j++) {//comparacao
					if(i==j){
						continue;
					}
					if(entrada2[v][i]>=entrada2[v][j]){
						maiorVetor = entrada2[v][i];
						menorVetor = entrada2[v][j];
					}else{
						maiorVetor = entrada2[v][j];
						menorVetor = entrada2[v][i];
					}
					if(i > j){
						maiorIndice = i;
						menorIndice = j;
					}else{
						maiorIndice = j;
						menorIndice = i;
					}
					if(maiorVetor - menorVetor == maiorIndice - menorIndice){
						avaliacao2[v]++;
					}
				}
			}
			avaliacao2[v]=avaliacao2[v]/2;
		}
		
		return avaliacao2;
	}
	

}
