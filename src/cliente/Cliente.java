package cliente;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import item.Item;
import servico.ServicoLista;
import servico.ServicoListaInterface;

public class Cliente {
	
	public long id;

	public static void main(String[] args) {
		try {
			ServicoListaInterface servicoLista = (ServicoListaInterface) Naming.lookup(ServicoLista.getURI());
			Cliente cli = new Cliente();
			cli.id = System.currentTimeMillis();
			servicoLista.setIdCliente(cli.id);//1
			servicoLista.trocaVez();
			servicoLista.teste();
			interfaceCliente(servicoLista, cli.id);
		} catch (MalformedURLException malfException) {
			System.out.println("\nMalformedURLException: " + malfException.toString());
		}  catch (RemoteException remException) {
			System.out.println("\nRemoteException: " + remException.toString());
		}  catch (NotBoundException notBoundException) {
			System.out.println("\nNotBoundException : " + notBoundException.toString());
		}  catch (Exception e) {
			System.out.println("\nException : " + e.toString());
		}
	}

	private static void limpaBuffer(Scanner s) {
		s.nextLine();
	}
	
	public static void interfaceCliente(ServicoListaInterface servico, long idCliente) throws RemoteException {
		Scanner s = new Scanner(System.in);
		String[] valores = {"Sair", "Adicionar Item", "Remover Item", "Mostrar Lista"};
		int op;
		String resp;
		if (servico.minhaVez()) {
			while(servico.idConfirmou() != idCliente) {
				Object opcao = JOptionPane.showInputDialog(null,  "Escolha uma opção", "Menu de Opções" , JOptionPane.DEFAULT_OPTION, null, valores, "Adicionar Item");
				if (opcao == "Adicionar Item") {
					JTextField nome = new JTextField(20);
					JTextField quant = new JTextField(20);
				
					JPanel panel = new JPanel();
					panel.add(new JLabel("Nome: "));
					panel.add(nome);
					panel.add(new JLabel("Quantidade: "));
					panel.add(quant);
					panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
					int result = JOptionPane.showConfirmDialog(null, panel,
					        "Adicionar Item", JOptionPane.OK_CANCEL_OPTION);
					checaOpcao(result);
				}
				
				if (opcao == "Remover Item") {
					JTextField nome = new JTextField(20);
				
					JPanel panel = new JPanel();
					panel.add(new JLabel("Nome: "));
					panel.add(nome);
					panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
					int result = JOptionPane.showConfirmDialog(null, panel,
					        "Remover Item", JOptionPane.OK_CANCEL_OPTION);
					checaOpcao(result);
					servico.removeItem(nome.getText());
				}
				
				if (opcao == "Mostrar Lista") {
					mostraLista(servico.getLista());
					interfaceCliente(servico, idCliente);
				}
				
				System.out.println("Confirmar lista? s/n");
				resp = s.nextLine();
				if (resp.equals("s")) {
					servico.confirmar(idCliente);
					servico.trocaVez();
					interfaceCliente(servico, idCliente);
				}	
				interfaceCliente(servico, idCliente);
			}
		} else {
			System.out.println("Espere a sua vez");
			while(!servico.minhaVez()) {
				
			}
			interfaceCliente(servico, idCliente);
		}
	}
	
	public static void mostraLista(List<Item> lista) throws RemoteException {
		int i = 1;
		System.out.println("=========== Lista De Casamento ===========");
		for (Item it: lista) {
			System.out.println("#"+i+"\tNome:"+it.getNome()+"\t Quantidade: "+it.getQuant());			
			i++;
		}
	}
	
	public static boolean checaOpcao(int result) {
		return result == JOptionPane.OK_OPTION;
	}

}
