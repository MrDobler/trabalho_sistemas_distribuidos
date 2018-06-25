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

	public static class Valores {
		public static String ADICIONAR_ITEM = "Adicionar Item";
		public static String REMOVER_ITEM = "Remover Item";
		public static String MOSTRAR_LISTA = "Mostrar Lista";
	}
	
	public UserInterface(ServicoListaInterface servico) {
		this.servico = servico;
	}
	
	public void interfaceCliente(long idCliente) throws RemoteException {
		Status statusDaAplicacao = servico.getStatus();
		
		while(statusDaAplicacao == Status.ESPERANDO_CONJUGES) {
			JOptionPane.showMessageDialog(null, "Esperando conjuge entrar.", "Mensagem Informativa", 0);
			statusDaAplicacao = servico.getStatus();
		}
		
		while(statusDaAplicacao == Status.RODANDO) {
			boolean meuTurno = servico.checkMeuTurno(idCliente);
			System.out.println(meuTurno);
			if (meuTurno) {
				servico.teste();
				
				MenuDeOpcoes(idCliente);

				int confirma = JOptionPane.showConfirmDialog(null, "Deseja confirmar a lista?");
				System.out.println("Confirma lista: " + confirma);
				
				if (confirma == JOptionPane.YES_OPTION) {
					System.out.println("ENTOUR");
					servico.confirmar(idCliente);
					servico.mudarTurno(idCliente);
				} else {
					System.out.println("NAO ENTORU");
				}
			} else {
				JOptionPane.showMessageDialog(null, "Esperando conjuge finalizar.", "Mensagem Informativa", 0);
			}
		}
		
		if(statusDaAplicacao == Status.FINALIZADO)
			mostraLista();
	}
	
	public void MenuDeOpcoes(long idCliente) throws RemoteException {
		String[] valores = {Valores.ADICIONAR_ITEM, Valores.REMOVER_ITEM, Valores.MOSTRAR_LISTA};
		String opcao = (String) JOptionPane.showInputDialog(null,  "Escolha uma opção - "+idCliente, "Menu de Opções" , JOptionPane.QUESTION_MESSAGE, null, valores, "Adicionar Item");
		System.out.println("Opcao escolhida: " + opcao);
		
		if (opcao == Valores.ADICIONAR_ITEM) {
			JTextField[] items = addItem();
			
			JTextField nome = items[0];
			JTextField quant = items[1];
			
			int quantidade = Integer.parseInt(quant.getText());
			
			servico.addItem(nome.getText(), quantidade);		 
		} else if (opcao == Valores.REMOVER_ITEM) {
			JTextField nome = new JTextField(20);
		
			JPanel panel = new JPanel();
			panel.add(new JLabel("Nome: "));
			panel.add(nome);
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			
			JOptionPane.showConfirmDialog(null, panel, "Remover Item", JOptionPane.OK_CANCEL_OPTION);
	
			servico.removeItem(nome.getText());
		} else if (opcao == Valores.MOSTRAR_LISTA) {
			mostraLista();
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
		
		int result = JOptionPane.showConfirmDialog(null, panel, "Remover Item", JOptionPane.OK_CANCEL_OPTION);
		
		if (result == JOptionPane.OK_OPTION) {
			JTextField[] resultado = {nome, quant};
	       	return resultado;
	    } else {
	    	JTextField[] resultado = {null};
	    	return resultado;
	    }
	}
	
	
	public void mostraLista() throws RemoteException {
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
		table.setBounds(50,143,397,138);
		
		JFrame frame = new JFrame("Lista de Casamento");
		frame.add(new JScrollPane(table));
        frame.setVisible(true);
        frame.pack();
	}
	
}
