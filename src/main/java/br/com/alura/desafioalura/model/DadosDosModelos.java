package br.com.alura.desafioalura.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosDosModelos(List<DadosDosVeiculos> modelos){
}
