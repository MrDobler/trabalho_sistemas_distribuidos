package gui;

import java.rmi.RemoteException;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import servico.ServicoListaInterface;
import shared.Item;
import shared.Status;

public class UserInterface {
	ServicoListaInterface servico;
	String nomeCliente;
	
	public static class Valores {
		public static String ADICIONAR_ITEM = "Adicionar Item";
		public static String MOSTRAR_LISTA = "Mostrar Lista";
	}
	
	public UserInterface(ServicoListaInterface servico) {
		this.servico = servico;
	}
	
	public void interfaceCliente(long idCliente) throws RemoteException {
		Status statusDaAplicacao = servico.getStatus();
		this.nomeCliente = servico.getNomeCliente(idCliente);
		
		do {
			statusDaAplicacao = servico.getStatus();
			try {
				sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while(statusDaAplicacao == Status.ESPERANDO_CONJUGES);

		
		do {
			boolean meuTurno = servico.checkMeuTurno(idCliente);
			System.out.println(meuTurno);
			if (meuTurno) {
				
				if (!servico.checkConfirmacao()) {
					int confirma;
					menuDeOpcoes(idCliente);
					
					confirma = JOptionPane.showConfirmDialog(null, "Deseja confirmar a lista?", null, JOptionPane.YES_OPTION);
					System.out.println("Confirma lista: " + confirma);
					
					if (confirma == JOptionPane.YES_OPTION) {
						System.out.println("ENTROU");
						servico.confirmar(idCliente);
						servico.mudarTurno(idCliente);
					} else if (confirma == JOptionPane.NO_OPTION) {
						continue;
					} else {
						statusDaAplicacao = Status.FINALIZADO;
					}
				}
				
				if (servico.possoProsseguir(idCliente)) {
					System.out.println("ClienteB confirmou");
				} else {
					while(!servico.liberarProxEtapa()) 
					try {
						System.out.println("Esperando proxima etapa");
						sleep(500);
					} catch (InterruptedException e) {
						
						e.printStackTrace();
					}
				}
	
				
				if (servico.podeFinalizar(idCliente)) {
					JOptionPane.showMessageDialog(null, nomeCliente+"\nO outro cliente confirmou a lista, clique em Ok para finalizar.", "Mensagem Informativa", JOptionPane.INFORMATION_MESSAGE);
					servico.liberarOutroCliente();
				} else {
					while (!servico.checkLiberacao()) {
						try {
							System.out.println("Esperando ultima liberação.");
							sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
	
				if (servico.checkConfirmacao()) {
					statusDaAplicacao = Status.FINALIZADO;
				}
			} else {
				try {
					sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		} while(statusDaAplicacao == Status.RODANDO);
		
		
		if(statusDaAplicacao == Status.FINALIZADO) {
			finalizar(idCliente);
		}
	}
	
	public void finalizar(long idCliente) throws RemoteException {
		JOptionPane.showMessageDialog(null, "Lista finalizada cliente: "+nomeCliente, "Mensagem Informativa", JOptionPane.PLAIN_MESSAGE);
		this.mostraLista(0);
		//Reseta as variáveis do Servidor
		servico.resetVars();
		//Atualiza o outro servidor com a lista vazia.
		Iterable<Item> lista = servico.getLista();
		servico.enviaParaOutroServidor(lista);
		System.exit(0);
	}
	
	public void erro() throws RemoteException {
		JOptionPane.showMessageDialog(null, "Aconteceu algum erro!\nO programa será finalizado.", "Mensagem de Erro", JOptionPane.ERROR_MESSAGE);
		servico.resetVars();
		System.exit(1);
	}
	
	public void menuDeOpcoes(long idCliente) throws RemoteException {
		String[] valores = {Valores.ADICIONAR_ITEM, Valores.MOSTRAR_LISTA};
		String opcao;
	
		opcao = (String) JOptionPane.showInputDialog(null,  "Escolha uma opção - "+nomeCliente, "Menu de Opções" , JOptionPane.QUESTION_MESSAGE, null, valores, "Adicionar Item");			
		
		System.out.println("Opcao escolhida: " + opcao);
		
		if (opcao == Valores.ADICIONAR_ITEM) {
			JTextField[] items = addItem();
			JTextField nome = null;
			JTextField quant = null;

			System.out.println("voltou com as vars");
			try {
				nome = items[0];
				quant = items[1];
				
				int quantidade = Integer.parseInt(quant.getText());
				
				servico.addItem(nome.getText(), quantidade);
				Iterable<Item> lista = servico.getLista();
				servico.enviaParaOutroServidor(lista);
				
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Os valores informados não estão corretos.", "Mensagem Informativa", JOptionPane.ERROR_MESSAGE);
				System.out.println(e.toString());
			}

		} else if (opcao == Valores.MOSTRAR_LISTA) {
			mostraLista(1);
		}
	}
	
	
	public JTextField[] addItem() {
		JTextField nome = new JTextField(20);
		JTextField quant = new JTextField(20);
	
		JPanel panel = new JPanel();
		
		panel.add(new JLabel("Nome: "));
		panel.add(nome);
		panel.add(new JLabel("Quantidade: "));
		panel.add(quant);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		int result = JOptionPane.showConfirmDialog(null, panel, "Adicionar Item", JOptionPane.OK_CANCEL_OPTION);
		
		if (result == JOptionPane.OK_OPTION) {
			JTextField[] resultado = {nome, quant};
	       	return resultado;
	    } else {
	    	JTextField[] resultado = {null};
	    	return resultado;
	    }
	}
	
	
	public void mostraLista(int end) throws RemoteException {
		Iterable<Item> lista = servico.getLista();
		
		Object[][] dadosLinhas = {};
		Object[] colunas = {"ID", "Nome", "Quantidade"};
		DefaultTableModel model;
		model = new DefaultTableModel(dadosLinhas, colunas);

		int i = 1;
		System.out.println("Iterando lista...");
		for (Item it : lista) {
			model.addRow(new Object[] {i, it.getNome().toString(), it.getQuant()});
			i++;
		}
		

		
		JTable table = new JTable(model);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setBounds(0,0,200,138);
		
		JFrame frame = new JFrame("Lista de Casamento");
		frame.add(new JScrollPane(table));
        frame.setVisible(true);
        frame.pack();
        
      
        if (end == 0)
        	JOptionPane.showMessageDialog(null, "Clique aqui para finalizar o programa", "Mensagem Informativa", JOptionPane.WARNING_MESSAGE);
        else
        	JOptionPane.showMessageDialog(null, "Clique aqui para prosseguir", "Mensagem Informativa", JOptionPane.WARNING_MESSAGE);
        
    	frame.dispose();
	}
	
	private static void sleep(long segundos) throws InterruptedException {
		Thread.currentThread();
		Thread.sleep(segundos);
	}
	
}
