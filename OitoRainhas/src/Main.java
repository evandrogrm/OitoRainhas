import java.util.ArrayList;
import java.util.Random;
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
		for (int T = 0; T < 100; T++) {
			avaliacao = avaliaPopulacao(entrada); // conta conflito entre rainhas
			selecionados = selecionaPais(avaliacao, entrada); // ordena pela avaliação e descarta metade
			entrada = recombinar(selecionados); // gera filhos com a metade gerada
			entrada = aplicaMutacao(entrada);
			avaliacao = avaliaPopulacao(entrada); // conta conflito entre rainhas
			entrada = selecionaProximaEpoca(avaliacao, entrada); // sera utilizado nos proximos metodos
			for (int i = 0; i < avaliacao.length; i++) {
				if(avaliacao[i]==0){
					System.out.println("Encontrado sequencia sem erros: "+entrada[i].toString());
				}
			}
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
		int[][] filhos = new int[10][selecionados2[0].length];
		ArrayList<Integer> listaJaCombinados = new ArrayList<>();
		// listaJaCombinados.add(selecionados2.clone());
		Random rnd = ThreadLocalRandom.current();
		int pontoDeCorte = 1 + rnd.nextInt(selecionados2[0].length - 1);
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
			populacao[i] = selecionados2[i].clone(); // pais
		}
		indiceFilhos = 0;
		for (int i = selecionados2.length; i < populacao.length; i++) {
			populacao[i] = filhos[indiceFilhos]; // filhos
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

	private static int[] avaliaPopulacao(int[][] entrada2) {
		int[] avaliacao2 = new int[entrada2.length];
		int[] teste = { 1, 4, 6, 8, 5, 2, 7, 3 };
		entrada2[0] = teste;
		int maior = 0;
		int menor = 0;
		int principal = 0;
		for (int i = 0; i < entrada2.length; i++) {
			for (int j = 0; j < entrada2[0].length; j++) {
				if (entrada2[i][j] == j + 1) {
					principal++;
//					System.out.println("Diagonal principal");
				}
				continua: for (int k = j + 1; k < entrada2[0].length; k++) {
					if (k > entrada2[0].length) {
						continue continua;
					}
					if (j - k == 1 || k - j == 1)
						if (entrada2[i][j] - entrada2[i][k] == 1 || entrada2[i][j] - entrada2[i][k] == -1) {
							avaliacao2[i] += 1;
//							System.out.println("entrada2[" + i + "][" + j + "] conflitou com entrada[" + i + "][" + k + "]");
						}
					if (entrada2[i][j] >= entrada2[i][k]) {
						maior = j + 1;
						menor = k + 1;
						if (maior - menor == entrada2[i][j]) {
							avaliacao2[i] += 1;
//							System.out.println("entrada2[" + i + "][" + j + "] conflitou com entrada[" + i + "][" + k + "]");
						}
					} else {
						maior = k + 1;
						menor = j + 1;
						if (maior - menor == entrada2[i][k]) {
							avaliacao2[i] += 1;
//							System.out.println("entrada2[" + i + "][" + j + "] conflitou com entrada[" + i + "][" + k + "]");
						}
					}
				}
			}
			if (principal > 1) {
				avaliacao2[i] += principal;
				principal = 0;
			}
		}
		return avaliacao2;
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

}
