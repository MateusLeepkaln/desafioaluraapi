package br.com.alura.desafioalura.principal;

import br.com.alura.desafioalura.model.DadosDosModelos;
import br.com.alura.desafioalura.model.DadosDosVeiculos;
import br.com.alura.desafioalura.model.DadosVeiculoEspecifico;
import br.com.alura.desafioalura.service.ConsumoDeApi;
import br.com.alura.desafioalura.service.ConverteOsDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Principal {
    private final String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";

    private final Scanner leitura = new Scanner(System.in);

    private final ConsumoDeApi consumo = new ConsumoDeApi();

    public void exibeMenu() {
        var menu = """
                \s
                 *** OPÇÕES ***
                 Carro
                \s
                 Moto
                \s
                 Caminhão
                \s
                 Digite a opção para consultar:\s
                \s""";

        System.out.println(menu);
        var opcao = leitura.nextLine();
        String endereco = "";

        if (opcao.toLowerCase().contains("carr")) {
            endereco = URL_BASE + "carros/marcas";
        } else if (opcao.toLowerCase().contains("mot")) {
            endereco = URL_BASE + "motos/marcas";
        } else if (opcao.toLowerCase().contains("caminha")) {
            endereco = URL_BASE + "caminhoes/marcas";
        } else {
            System.out.println("Endereço não encontrado!");
        }
        var json = consumo.obterDados(endereco);
//        System.out.println(json);

        ConverteOsDados conversor = new ConverteOsDados();
        List<DadosDosVeiculos> veiculo = conversor.obterLista(json, DadosDosVeiculos.class);
        veiculo.forEach(System.out::println);

        System.out.println("Digite o código da marca para pesquisar: ");
        var codigoDaMarca = leitura.nextLine();
        endereco = endereco + "/" + codigoDaMarca + "/modelos";
        json = consumo.obterDados(endereco);

        var dadosDosModelos = conversor.obterDados(json, DadosDosModelos.class);
        System.out.println("Modelos da marca: ");
        dadosDosModelos.modelos().stream()
                .sorted(Comparator.comparing(DadosDosVeiculos::codigo))
                .forEach(System.out::println);

        System.out.println("Digite o nome do veículo para pesquisar: ");
        var nomeDoVeiculo = leitura.nextLine();

        List<DadosDosVeiculos> modelosFiltrados = dadosDosModelos.modelos().stream()
                .filter(m -> m.nome().toLowerCase().contains(nomeDoVeiculo.toLowerCase()))
                .toList();

        if (modelosFiltrados.isEmpty()) {
            System.out.println("Nenhuum modelo foi encontrado!");
            return;
        }

        if (modelosFiltrados.size() == 1) {
            System.out.println("Apenas um modelo foi encontrado! Seus dados são: ");
            var modeloUnico = modelosFiltrados.get(0);
            System.out.println("Modelo único encontrado: " + modeloUnico);

            var codigoDoModelo = modeloUnico.codigo();
            endereco = endereco + "/" + codigoDoModelo + "/anos";

            json = consumo.obterDados(endereco);
            List<DadosDosVeiculos> anos = conversor.obterLista(json, DadosDosVeiculos.class);

            List<DadosVeiculoEspecifico> veiculos = new ArrayList<>();

            for (DadosDosVeiculos ano : anos) {
                var anoEndereco = endereco + "/" + ano.codigo();
                json = consumo.obterDados(anoEndereco);
                var dadosVeiculoEspecifico = conversor.obterDados(json, DadosVeiculoEspecifico.class);
                veiculos.add(dadosVeiculoEspecifico);
            }
            veiculos.forEach(System.out::println);
            return;
        }

        System.out.println("Modelos encontrados: ");
        modelosFiltrados.forEach(System.out::println);

        System.out.println("Agora, digite o código do modelo do veículo para pesquisar: ");
        var codigoDoModelo = leitura.nextLine();
        endereco = endereco + "/" + codigoDoModelo + "/anos";

        json = consumo.obterDados(endereco);
        List<DadosDosVeiculos> anos = conversor.obterLista(json, DadosDosVeiculos.class);

        List<DadosVeiculoEspecifico> veiculos = new ArrayList<>();
        for (DadosDosVeiculos ano : anos) {
            var anoEndereco = endereco + "/" + ano.codigo();
            json = consumo.obterDados(anoEndereco);
            var dadosVeiculoEspecifico = conversor.obterDados(json, DadosVeiculoEspecifico.class);
            veiculos.add(dadosVeiculoEspecifico);
        }
        veiculos.forEach(System.out::println);



    }
}
