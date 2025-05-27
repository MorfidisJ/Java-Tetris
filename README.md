# Java Tetris Game

## Overview
This is a modern implementation of the classic Tetris game using Java Swing. The game features a clean, modern UI with smooth animations and all the standard Tetris mechanics including piece holding, ghost pieces, and scoring system.

## Features
- 🎮 Classic Tetris gameplay
- 🎨 Modern UI with smooth animations
- 👻 Ghost piece preview
- 💾 Hold piece functionality
- 📊 Score tracking and level progression
- 🎯 Next piece preview
- ⏯️ Pause functionality
- 🎨 Custom block colors and styling

## Game Controls
- **Left Arrow**: Move piece left
- **Right Arrow**: Move piece right
- **Down Arrow**: Soft drop
- **Up Arrow**: Rotate piece
- **Space**: Hard drop
- **C**: Hold piece
- **P**: Pause game

## Game Mechanics

### Scoring System
The scoring system follows the classic Tetris rules:
- 1 line cleared: 100 × level
- 2 lines cleared: 300 × level
- 3 lines cleared: 500 × level
- 4 lines cleared: 800 × level (Tetris)
- Hard drop: 2 points per cell dropped

### Level Progression
- Level increases every 10 lines cleared
- Game speed increases with each level
- Starting speed: 500ms per drop
- Minimum speed: 100ms per drop
- Speed reduction: 50ms per level

## Implementation Details

### Class Structure
```mermaid
classDiagram
    class Tetris {
        -GamePanel gamePanel
        -HoldPanel holdPanel
        -NextPanel nextPanel
        -Timer timer
        +main(String[] args)
        -setupKeyBindings()
        -updateScore(int lines)
        -gameOver()
        -resetGame()
    }
    class GamePanel {
        -Color[][] board
        -Tetromino currentPiece
        -Tetromino nextPiece
        -Tetromino heldPiece
        +moveLeft()
        +moveRight()
        +moveDown()
        +rotate()
        +hardDrop()
        +holdPiece()
    }
    class Tetromino {
        <<abstract>>
        #boolean[][] shape
        #Color color
        #int x, y
        +getType()
        +getRotated()
        +copy()
    }
    Tetris --> GamePanel
    GamePanel --> Tetromino
    Tetromino <|-- TetrominoI
    Tetromino <|-- TetrominoJ
    Tetromino <|-- TetrominoL
    Tetromino <|-- TetrominoO
    Tetromino <|-- TetrominoS
    Tetromino <|-- TetrominoT
    Tetromino <|-- TetrominoZ
```

### Game Flow
```mermaid
flowchart TD
    A[Start Game] --> B[Initialize Board]
    B --> C[Generate First Piece]
    C --> D[Game Loop]
    D --> E{User Input}
    E -->|Move Left| F[Check Collision]
    E -->|Move Right| F
    E -->|Rotate| F
    E -->|Soft Drop| F
    E -->|Hard Drop| G[Drop to Bottom]
    E -->|Hold| H[Swap Pieces]
    F -->|Valid Move| I[Update Position]
    F -->|Invalid Move| D
    I --> J[Check Lines]
    J -->|Lines Found| K[Clear Lines]
    K --> L[Update Score]
    L --> M[Generate New Piece]
    M --> N{Game Over?}
    N -->|Yes| O[End Game]
    N -->|No| D
```

### Piece Types
```mermaid
graph LR
    I[Tetromino I] --> |Cyan| IShape[****]
    J[Tetromino J] --> |Blue| JShape[*<br/>***]
    L[Tetromino L] --> |Orange| LShape[  *<br/>***]
    O[Tetromino O] --> |Yellow| OShape[**<br/>**]
    S[Tetromino S] --> |Green| SShape[ **<br/>**]
    T[Tetromino T] --> |Purple| TShape[ *<br/>***]
    Z[Tetromino Z] --> |Red| ZShape[**<br/> **]
```

## Technical Details

### Board Dimensions
- Width: 10 cells
- Height: 20 cells
- Block Size: 30 pixels
- Border Width: 5 pixels

### Piece Rotation System
The game implements a wall kick system for piece rotation:
1. Attempts standard rotation
2. If blocked, tries shifting left/right
3. If still blocked, tries shifting up
4. If all attempts fail, rotation is cancelled

### Collision Detection
The game uses a grid-based collision system:
- Checks board boundaries
- Checks for overlapping pieces
- Handles piece placement
- Manages line clearing

## Requirements
- Java Runtime Environment (JRE) 8 or higher
- Java Development Kit (JDK) 8 or higher for development

## How to Run
1. Compile the Java file:
```bash
javac tetris.java
```
2. Run the compiled class:
```bash
java Tetris
```

## Future Improvements
- High score system
- Custom themes
- Sound effects
- Multiplayer support
- Mobile port

## Credits
- Game Design & Implementation: Ioannis Morfidis
- Original Tetris Concept: Alexey Pajitnov
- Java Swing Framework: Oracle Corporation

## License
This project is open source and available under the MIT License.

---
## Author
John Morfidis 
