package org.springframework.ai.openai.samples.helloworld.output;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ActorsFilms {

	private String actor;
	private List<String> movies;

	@Override
	public String toString() {
		return "ActorsFilms{" + "actor='" + actor + '\'' + ", movies=" + movies + '}';
	}

}
