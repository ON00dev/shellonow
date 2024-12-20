function OnStart() {
    // Solicitar permissões de leitura e escrita no armazenamento
    app.CheckPermission("WriteExternalStorage");
    app.CheckPermission("ReadExternalStorage");

    // Criar layout principal
    let layout = app.CreateLayout("linear", "VCenter,FillXY");
    let colorPattern = "#1E1E2E";
    layout.SetBackColor(colorPattern);

    // Criar uma barra superior estilizada para o Título do app
    let topBar1 = app.CreateLayout("linear", "Horizontal");
    topBar1.SetBackColor("#27293D");
    topBar1.SetSize(1, 0.08);
    
    // Criar o texto do título com estilo centralizado
    let Title = app.CreateText("ShellOnow", 1, 0.1, "Bold");
    Title.SetTextColor("#FFFFFF");
    Title.SetTextSize(30); // Fonte grande
    Title.SetPadding(0, 0.02, 0, 0); // Centraliza verticalmente no layout
    topBar1.AddChild(Title);
    
    // Adicionar a barra superior ao layout principal
    layout.AddChild(topBar1);

    // Criar uma barra superior estilizada para o botão de importação
    let topBar2 = app.CreateLayout("linear", "Horizontal");
    topBar2.SetBackColor(colorPattern);
    topBar2.SetSize(1, 0.07);

    let btnSelectFile = app.CreateButton("\ud83d\udcc2 Importar", 0.3, 0.05, "Custom");
    btnSelectFile.SetBackColor("#444466");
    btnSelectFile.SetTextColor("#FFFFFF");
    btnSelectFile.SetOnTouch(() => {
        selectShellScript();
    });
    topBar2.AddChild(btnSelectFile);
    layout.AddChild(topBar2);

    // Campo de entrada para comandos Shell
    let inputCommand = app.CreateTextEdit("", 0.9, 0.04);
    inputCommand.SetHint("shell@onow:~$");
    inputCommand.SetBackColor("#2A2D40");
    inputCommand.SetTextColor("#FFFFFF");
    inputCommand.SetCursorColor("#FFAA33");
    layout.AddChild(inputCommand);

    // Botão para executar o comando
    let btnExecute = app.CreateButton("\u25b6\ufe0f Executar Comando", 0.4, 0.06, "Custom");
    btnExecute.SetBackColor("#444466");
    btnExecute.SetTextColor("#FFFFFF");
    btnExecute.SetOnTouch(() => {
        let command = inputCommand.GetText();
        if (command.trim() !== "") {
            executeShellCommand(command);
        } else {
            app.ShowPopup("\u26a0\ufe0f Por favor, insira um comando.", "Short");
        }
    });
    layout.AddChild(btnExecute);

    // Campo para exibir a saída do comando
    let output = app.CreateTextEdit("", 0.9, 0.3, "ReadOnly,MultiLine");
    output.SetBackColor("#1A1A1E");
    output.SetTextColor("#00FF7F");
    output.SetCursorColor("#00FF7F");
    output.SetHint("Saída do comando será exibida aqui");
    layout.AddChild(output);

    // Campo para visualizar o conteúdo do arquivo .sh
    let fileContent = app.CreateTextEdit("", 0.9, 0.3, "ReadOnly");
    fileContent.SetBackColor("#2A2D40");
    fileContent.SetTextColor("#FFFFFF");
    fileContent.SetCursorColor("#FFAA33");
    fileContent.SetHint("Conteúdo do arquivo shell será exibido aqui");
    layout.AddChild(fileContent);

    // Botão para executar o arquivo .sh
    let btnRunFile = app.CreateButton("\u25b6\ufe0f Executar Arquivo shell", 0.4, 0.06, "Custom");
    btnRunFile.SetBackColor("#444466");
    btnRunFile.SetTextColor("#FFFFFF");
    btnRunFile.SetOnTouch(() => {
        let commands = fileContent.GetText();
        if (commands.trim() !== "") {
            executeShellCommand(commands);
        } else {
            app.ShowPopup("\u26a0\ufe0f Nenhum conteúdo para executar.", "Short");
        }
    });
    layout.AddChild(btnRunFile);

    // Adicionar layout à tela
    app.AddLayout(layout);

    // Função para executar comandos Shell
    function executeShellCommand(command) {
        app.ShowPopup("\u23f3 Executando comando...");

        try {
            // Executar o comando via app.SysExec()
            let result = app.SysExec(command);
            if (result) {
                output.SetText(result);
            } else {
                output.SetText("\u26a0\ufe0f Erro ao executar comando ou saída vazia.");
            }
        } catch (e) {
            output.SetText("\u274c Erro: " + e.message);
        }
    }

    // Função para selecionar e carregar arquivo .sh
    function selectShellScript() {
        app.ChooseFile("Selecione um arquivo .sh", ".sh", (file) => {
            app.ShowPopup("\ud83d\udcc1 Arquivo selecionado: " + file);
            // Ler o conteúdo do arquivo
            let content = app.ReadFile(file);
            if (content) {
                fileContent.SetText(content);
            } else {
                app.ShowPopup("\u26a0\ufe0f Erro ao ler o arquivo.");
            }
        });
    }
}
