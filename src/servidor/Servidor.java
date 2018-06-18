package servidor;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

import servico.ServicoLista;
import servico.ServicoListaInterface;

public class Servidor {
	

	public Servidor () {
		try {
			ServicoListaInterface servico = new ServicoLista();
			Naming.rebind(ServicoLista.getURI(), servico);
			System.out.println("Servidor no Ar!");
		} catch (MalformedURLException malformedExcep) {
			System.out.println("Erro: " + malformedExcep);
			System.exit(0);
		} catch (RemoteException remoteExcep) {
			System.out.println("Erro:" + remoteExcep);
			System.exit(1);
		}
	}

	public static void main(String[] args) {
		new Servidor();
		
	}
}
