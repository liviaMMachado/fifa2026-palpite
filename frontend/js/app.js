const API_BASE = window.location.origin;

const screens = {
  inicio: document.getElementById("screen-inicio"),
  jogos: document.getElementById("screen-jogos"),
  placar: document.getElementById("screen-placar"),
  sucesso: document.getElementById("screen-sucesso"),
  final: document.getElementById("screen-final"),
};

const listaPartidas = document.getElementById("lista-partidas");
const placarPartidaNome = document.getElementById("placar-partida-nome");
const scoreCasa = document.getElementById("score-casa");
const scoreVisitante = document.getElementById("score-visitante");

let partidas = [];
let partidaSelecionada = null;
let golsCasa = 0;
let golsVisitante = 0;
let timerSucesso = null;

function showScreen(name) {
  Object.values(screens).forEach((screen) => screen.classList.remove("active"));
  screens[name].classList.add("active");
}

function formatScore(value) {
  return String(value).padStart(2, "0");
}

function atualizarPlacar() {
  scoreCasa.textContent = formatScore(golsCasa);
  scoreVisitante.textContent = formatScore(golsVisitante);
}

async function carregarPartidas() {
  try {
    const response = await fetch(`${API_BASE}/api/partidas`);
    if (!response.ok) {
      throw new Error("Nao foi possivel carregar os jogos do dia.");
    }
    partidas = await response.json();
    renderizarPartidas();
  } catch (error) {
    const previewIntelliJ = window.location.port === "63342" || window.location.search.includes("_ijt=");
    if (previewIntelliJ) {
      throw new Error(
        "Abra o app pelo servidor Java em http://localhost:8080\n\n" +
        "1. Execute run-server.bat na pasta do projeto\n" +
        "2. Acesse http://localhost:8080 no navegador"
      );
    }
    throw new Error(
      "Nao foi possivel carregar os jogos do dia.\n\n" +
      "Execute run-server.bat e abra http://localhost:8080"
    );
  }
}

function renderizarPartidas() {
  listaPartidas.innerHTML = "";

  partidas.forEach((partida) => {
    const card = document.createElement("button");
    card.type = "button";
    card.className = "partida-card";
    card.innerHTML = `
      <div class="partida-times">
        <span>${partida.timeCasa}</span>
        <span>${partida.timeVisitante}</span>
      </div>
      <span class="partida-grupo">${partida.grupo}</span>
      <div class="partida-horario">
        <span>Hoje</span>
        <span>${partida.horario}</span>
      </div>
      <span class="partida-arrow">&gt;</span>
    `;

    card.addEventListener("click", () => selecionarPartida(partida));
    listaPartidas.appendChild(card);
  });
}

function selecionarPartida(partida) {
  partidaSelecionada = partida;
  golsCasa = 0;
  golsVisitante = 0;
  atualizarPlacar();
  placarPartidaNome.textContent = `${partida.timeCasa} X ${partida.timeVisitante}`;
  showScreen("placar");
}

function ajustarGols(lado, delta) {
  if (lado === "casa") {
    golsCasa = Math.max(0, Math.min(99, golsCasa + delta));
  } else {
    golsVisitante = Math.max(0, Math.min(99, golsVisitante + delta));
  }
  atualizarPlacar();
}

function irParaTelaFinal() {
  if (timerSucesso) {
    clearTimeout(timerSucesso);
    timerSucesso = null;
  }
  showScreen("final");
}

async function enviarPalpite() {
  if (!partidaSelecionada) {
    return;
  }

  const btnEnviar = document.getElementById("btn-enviar");
  btnEnviar.disabled = true;

  try {
    const response = await fetch(`${API_BASE}/api/palpites`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        partidaId: partidaSelecionada.id,
        golsCasa,
        golsVisitante,
      }),
    });

    if (!response.ok) {
      throw new Error("Erro ao enviar palpite.");
    }

    showScreen("sucesso");

    if (timerSucesso) {
      clearTimeout(timerSucesso);
    }

    timerSucesso = setTimeout(irParaTelaFinal, 10000);
  } catch (error) {
    alert(error.message);
  } finally {
    btnEnviar.disabled = false;
  }
}

document.getElementById("btn-iniciar").addEventListener("click", async () => {
  try {
    await carregarPartidas();
    showScreen("jogos");
  } catch (error) {
    alert(error.message);
  }
});

document.querySelectorAll(".score-btn").forEach((button) => {
  button.addEventListener("click", () => {
    const lado = button.dataset.side;
    const delta = button.classList.contains("score-up") ? 1 : -1;
    ajustarGols(lado, delta);
  });
});

document.getElementById("btn-enviar").addEventListener("click", enviarPalpite);

document.getElementById("btn-avancar-sucesso").addEventListener("click", irParaTelaFinal);

document.getElementById("btn-outro-palpite").addEventListener("click", async () => {
  partidaSelecionada = null;
  golsCasa = 0;
  golsVisitante = 0;

  try {
    await carregarPartidas();
    showScreen("jogos");
  } catch (error) {
    alert(error.message);
  }
});

atualizarPlacar();
