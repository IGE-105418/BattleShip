<?php
session_start();

$winningLines = [
    [0, 1, 2], [3, 4, 5], [6, 7, 8],
    [0, 3, 6], [1, 4, 7], [2, 5, 8],
    [0, 4, 8], [2, 4, 6]
];

function getWinner(array $board, array $winningLines): ?string
{
    foreach ($winningLines as [$a, $b, $c]) {
        if (
            $board[$a] !== '' &&
            $board[$a] === $board[$b] &&
            $board[$b] === $board[$c]
        ) {
            return $board[$a];
        }
    }

    return in_array('', $board, true) ? null : 'draw';
}

function getAvailableCells(array $board): array
{
    $available = [];

    foreach ($board as $index => $value) {
        if ($value === '') {
            $available[] = $index;
        }
    }

    return $available;
}

function makeComputerMove(array &$board, array $winningLines): void
{
    $available = getAvailableCells($board);

    foreach (['O', 'X'] as $symbol) {
        foreach ($available as $index) {
            $simulation = $board;
            $simulation[$index] = $symbol;

            if (getWinner($simulation, $winningLines) === $symbol) {
                $board[$index] = 'O';
                return;
            }
        }
    }

    if ($board[4] === '') {
        $board[4] = 'O';
        return;
    }

    $corners = array_values(array_intersect([0, 2, 6, 8], $available));
    $choices = !empty($corners) ? $corners : $available;

    if (!empty($choices)) {
        $board[$choices[array_rand($choices)]] = 'O';
    }
}

if (!isset($_SESSION['board']) || isset($_POST['reset'])) {
    $_SESSION['board'] = array_fill(0, 9, '');
}

$board = &$_SESSION['board'];
$result = getWinner($board, $winningLines);

if (isset($_POST['cell']) && $result === null) {
    $cell = (int) $_POST['cell'];

    if (isset($board[$cell]) && $board[$cell] === '') {
        $board[$cell] = 'X';
        $result = getWinner($board, $winningLines);

        if ($result === null) {
            makeComputerMove($board, $winningLines);
            $result = getWinner($board, $winningLines);
        }
    }
}
?>

<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <title>Tic-Tac-Toe com Docker</title>

    <style>
        body {
            margin: 0;
            font-family: Arial, sans-serif;
            text-align: center;
            background: #f3f6fb;
        }

        main {
            max-width: 520px;
            margin: 55px auto;
            padding: 30px;
            border-radius: 18px;
            background: white;
            box-shadow: 0 10px 28px rgba(0, 0, 0, .12);
        }

        .board {
            display: grid;
            grid-template-columns: repeat(3, 95px);
            gap: 8px;
            justify-content: center;
            margin: 24px 0;
        }

        .cell {
            height: 95px;
            border: 1px solid #cbd4e1;
            border-radius: 10px;
            font-size: 38px;
            cursor: pointer;
            background: #eef3fa;
        }

        .reset {
            padding: 11px 18px;
            border: none;
            border-radius: 8px;
            color: white;
            cursor: pointer;
            background: #2457a6;
        }
    </style>
</head>

<body>
<main>
    <h1>Tic-Tac-Toe</h1>

    <p>Tu és <strong>X</strong>. O computador é <strong>O</strong>.</p>

    <?php if ($result === 'X'): ?>
        <h2>Ganhaste!</h2>
    <?php elseif ($result === 'O'): ?>
        <h2>O computador ganhou.</h2>
    <?php elseif ($result === 'draw'): ?>
        <h2>Empate.</h2>
    <?php endif; ?>

    <form method="post">
        <div class="board">
            <?php for ($index = 0; $index < 9; $index++): ?>
                <button
                    class="cell"
                    name="cell"
                    value="<?= $index ?>"
                    <?= ($board[$index] !== '' || $result !== null) ? 'disabled' : '' ?>
                >
                    <?= htmlspecialchars($board[$index]) ?>
                </button>
            <?php endfor; ?>
        </div>
    </form>

    <form method="post">
        <button class="reset" name="reset" value="1">Reiniciar jogo</button>
    </form>
</main>
</body>
</html>