package cliente;

import java.awt.BorderLayout;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

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
	
	public static void interfaceCliente(ServicoListaInterface servico, long idCliente) throws RemoteException {
		String[] valores = {"Sair", "Adicionar Item", "Remover Item", "Mostrar Lista"};
		if (servico.minhaVez()) {
			while(servico.idConfirmou() != idCliente) {
				Object opcao = JOptionPane.showInputDialog(null,  "Escolha uma opção - "+idCliente, "Menu de Opções" , JOptionPane.DEFAULT_OPTION, null, valores, "Adicionar Item");
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
					servico.trocaVez();
					interfaceCliente(servico, idCliente);
				}
				
				if (checaOpcao(confirmar())) {
					servico.confirmar(idCliente);
					servico.trocaVez();
					interfaceCliente(servico, idCliente);					
				} else {
					interfaceCliente(servico, idCliente);					
				}
				
			}
		} else {
			JOptionPane.showMessageDialog(null, "Espere a sua vez.", "Mensagem Informativa", 0);
			while(!servico.minhaVez()) {
				
			}
			interfaceCliente(servico, idCliente);
		}
	}
	
	public static void mostraLista(List<Item> lista) throws RemoteException {
		JFrame frame = new JFrame("Mostrar Lista de Casamento");
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		JTable table = new JTable(new DefaultTableModel(null, new Object[]{"ID", "Nome", "Quantidade"}));
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		
		JScrollPane tableContainer = new JScrollPane(table);
		panel.add(tableContainer, BorderLayout.CENTER);
        frame.getContentPane().add(panel);

        frame.pack();
        frame.setVisible(true);
		
		int i = 1;
		for (Item it: lista) {
			model.addRow(new Object[] {i, it.getNome(), it.getQuant()});
			i++;
		}
	}
	
	public static boolean checaOpcao(int result) {
		return result == JOptionPane.OK_OPTION;
	}
	
	public static int confirmar() {
		int escolha = JOptionPane.showOptionDialog(null, "Deseja confirmar a lista?", "Confirmação", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null, null, null);
		return escolha;
	}

}
