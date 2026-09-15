# Manual do Utilizador - LogiTrack
**Sistema de Gestão Ferroviária / Rail Management System**

---

## 🇵🇹 

### 1. Introdução
O **LogiTrack** é um sistema de gestão ferroviária desenhado para facilitar a criação de rotas, gestão de mercadorias (freights) e agendamento de comboios. O sistema garante a integridade das operações verificando a disponibilidade de recursos (vagões e locomotivas) e a conectividade lógica entre estações.

### 2. Navegação Principal
O menu lateral esquerdo permite navegar entre as duas funcionalidades centrais:
* **Route Manager:** Para criação e definição de caminhos.
* **Schedules:** Para gestão de carga, criação de comboios e monitorização da rede.

No canto inferior esquerdo, existe um botão **Exit** para fechar a aplicação.

---

### 3. Gestor de Rotas (Route Manager)
Este módulo permite definir caminhos ferroviários. Existem dois modos de operação, selecionáveis no topo do ecrã:

#### A. Gerador Automático (Automatic Generator)
Ideal para encontrar o caminho mais rápido entre dois pontos.
1.  **Origem e Destino:** Selecione a estação de partida (Origin) e a estação de chegada (Destination) nas caixas de seleção.
    * *Nota:* As caixas de texto permitem escrever para filtrar as estações rapidamente.
2.  **Cálculo:** Clique em **"Calculate Path"**. O sistema irá calcular o caminho mais curto (Shortest Path) baseado nas conexões existentes.
3.  **Criação da Rota:** Após o cálculo, poderá transformar este caminho numa Rota oficial (Route) definindo uma data para a viagem.

#### B. Construtor Manual (Manual Builder)
Permite ao utilizador ter controlo total sobre o trajeto, estação por estação.
1.  **Início:** Selecione a estação de origem na lista.
2.  **Adicionar Estações:** Utilize o botão **"Add Station"**.
    * *Regra de Conectividade:* O sistema apenas permite selecionar estações que tenham uma ligação física direta com a estação anterior.
    * *Estações Isoladas:* Estações sem conexões não aparecerão como opções válidas para continuação.
3.  **Reset:** Se cometer um erro, utilize o botão **"Reset"** para limpar o caminho atual.
4.  **Salvar:** Ao finalizar o desenho da linha, clique em **"Save Manual Path"** e atribua uma data para a converter numa Rota.

---

### 4. Agendamentos e Comboios (Schedules)
No menu **LogiTrack Manager**, o fluxo de trabalho divide-se em duas etapas principais e um painel de monitorização.

#### Passo 1: Criar Mercadoria Independente (Create Independent Freight)
Aqui define-se *o que* vai ser transportado.
1.  Selecione a **Origem** e o **Destino** (deve coincidir com uma rota existente).
2.  O sistema apresentará uma lista de vagões (Wagons) compatíveis.
3.  **Seleção de Vagões:** Selecione os vagões desejados.
    * *Disponibilidade:* Vagões marcados a vermelho (**IN TRANSIT**) não podem ser selecionados até à hora indicada na etiqueta. Apenas vagões a verde (**AVAILABLE**) estão livres.
4.  Clique em **"Create Freight"** para agrupar os vagões.

#### Passo 2: Criar Horário do Comboio (Create Train Schedule)
Aqui associa-se a carga a uma locomotiva e a uma rota.
1.  **Selecionar Rota:** Escolha uma rota previamente criada.
2.  **Selecionar Mercadoria:** Escolha o Freight (conjunto de vagões) criado no passo anterior.
3.  **Selecionar Locomotiva:** Associe uma locomotiva ao comboio.
    * *Atenção:* Tal como os vagões, locomotivas em viagem não estarão disponíveis na lista de seleção.
4.  Clique em **"Confirm Schedule"** para lançar o comboio.

#### Monitorização da Rede (Network Status)
O painel direito oferece uma visão geral do sistema:
* **Schedule Monitor:** Lista os comboios ativos com 3 estados:
    1.  🟡 **WAITING:** O comboio está parado, a aguardar a hora de partida ou libertação de via.
    2.  🟢 **RUNNING:** O comboio está em trânsito (mostra hora prevista de chegada).
    3.  ⚪ **FINISHED:** A viagem foi concluída.
* **Routes Available:** Lista rápida das rotas configuradas no sistema.

---

### 5. Utilitários do Sistema
* **System Logs:** Localizado na parte inferior do ecrã, este painel regista todas as ações (sucesso na criação de rotas, erros de validação, mudanças de modo).
* **Reset Database:** Um botão vermelho localizado no canto inferior direito do menu *Schedules*.
    * *Função:* Permite limpar a base de dados de testes de forma segura.
    * *O que apaga:* Elimina apenas Comboios, Rotas e Freights. As estações e conexões físicas mantêm-se intactas.

---
# User Manual - LogiTrack
**Rail Management System**
---
## 🇬🇧 

### 1. Introduction
**LogiTrack** is a rail management system designed to streamline route creation, freight management, and train scheduling. The system ensures operational integrity by validating resource availability (wagons and locomotives) and logical station connectivity.

### 2. Main Navigation
The left-hand sidebar allows navigation between the two core features:
* **Route Manager:** For path definition and route creation.
* **Schedules:** For freight management, train scheduling, and network monitoring.

An **Exit** button is available at the bottom left to close the application.

---

### 3. Route Manager
This module allows users to define railway paths using two distinct modes, selectable at the top of the screen:

#### A. Automatic Generator
Ideal for finding the quickest route between two points.
1.  **Origin and Destination:** Select the starting station and the destination station from the dropdowns.
    * *Note:* The text boxes support typing to filter stations quickly.
2.  **Calculation:** Click **"Calculate Path"**. The system will compute the Shortest Path based on existing connections.
3.  **Route Creation:** Once calculated, you can convert this path into an official "Route" by assigning a date to it.

#### B. Manual Builder
 grants the user full control to build a path station-by-station.
1.  **Start:** Select the origin station.
2.  **Add Stations:** Use the **"Add Station"** button to extend the path.
    * *Connectivity Rule:* The system only allows the selection of stations that are physically connected to the previous station.
    * *Isolated Stations:* Stations without connections will not appear as valid options.
3.  **Reset:** Use the **"Reset"** button to clear the current path if mistakes are made.
4.  **Save:** Upon completion, click **"Save Manual Path"** and assign a date to convert it into a Route.

---

### 4. Schedules (LogiTrack Manager)
In the **Schedules** view, the workflow is divided into two creation steps and a monitoring panel.

#### Step 1: Create Independent Freight
Define *what* is being transported.
1.  Select **Origin** and **Destination** (must match an existing route context).
2.  The system displays a list of compatible Wagons.
3.  **Wagon Selection:** Choose the desired wagons.
    * *Availability:* Wagons marked in red (**IN TRANSIT**) cannot be selected until the time timestamp shown. Only green (**AVAILABLE**) wagons can be used.
4.  Click **"Create Freight"** to bundle the wagons.

#### Step 2: Create Train Schedule
Assign the freight to a locomotive and a route.
1.  **Select Route:** Choose a previously created route.
2.  **Select Freight:** Choose the Freight (wagon group) created in Step 1.
3.  **Select Locomotive:** Assign an engine to the train.
    * *Note:* Locomotives currently on a trip will not be available for selection.
4.  Click **"Confirm Schedule"** to schedule the train.

#### Network Status
The right-hand panel provides a comprehensive system overview:
* **Schedule Monitor:** Lists active trains with 3 status types:
    1.  🟡 **WAITING:** Train is parked, waiting for departure time or track clearance.
    2.  🟢 **RUNNING:** Train is in transit (shows estimated arrival time).
    3.  ⚪ **FINISHED:** The trip has concluded.
* **Routes Available:** A quick reference list of routes currently configured.

---

### 5. System Utilities
* **System Logs:** Located at the bottom of the screen, this panel logs all events (route creation success, validation errors, mode switching).
* **Reset Database:** A red button located at the bottom right of the *Schedules* menu.
    * *Function:* Safely clears the testing database.
    * *Scope:* It deletes Trains, Routes, and Freights only. Stations and physical track data remain untouched.