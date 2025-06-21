package br.com.alura.desafioalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public record DadosDosVeiculos(@JsonAlias("codigo") String codigo,
                              @JsonAlias("nome") String nome) {
}
