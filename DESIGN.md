# Sistema de diseño — Outfit Cyberdeck

## Lenguaje visual

Las pantallas de selección y clasificación usan una estética pixel-art propia,
con controles compactos y jerarquía de videojuego retro en lugar de componentes
Material convencionales.

Elementos clave a preservar:

- **Marco de madera de doble borde**: un borde exterior oscuro grueso, uno interior
  más claro y fino, relleno de pergamino cálido. No es un `Card` con `elevation`;
  es un marco pintado a mano, sin sombra difusa, sin blur.
- **Retrato grande a la izquierda** dentro de su propio marco anidado, con flechas
  ◄ ▶ debajo para "rotar" — en nuestro caso, la foto de la prenda ya está fija, así
  que esas flechas no aplican, pero el retrato ocupa el mismo rol visual dominante.
- **Filas de atributo a la derecha**: `◄ Etiqueta   valor actual ▶`, etiqueta en tinta
  roja/marrón oscuro, flechas naranjas rellenas con punta más oscura.
- **Sliders arcoíris**: usados para Skin/Eye/Hair/Pants Color en el juego. El fondo
  del track es un degradado horizontal completo (rojo→amarillo→verde→cian→azul→
  magenta→rojo), el knob es un círculo oscuro con centro claro.
- **Botón OK**: esquina inferior derecha, verde, con corte de esquina (no redondeado).
- **Checkbox "Skip Intro"**: cuadrado simple sin redondeo, borde oscuro.
- **Dado decorativo** en la esquina superior izquierda del marco — detalle de sabor,
  no funcional; en nuestra versión puede sustituirse por un icono de percha o similar
  si se quiere un guiño temático a "ropa" sin copiar el asset original del juego.

## Paleta (ya en `PixelTheme.kt`, no la dupliques con literales sueltos)

| Token | Hex | Uso |
|---|---|---|
| `WoodDark` | `#6B4423` | Borde exterior de marcos, fondo de pantalla |
| `WoodMid` | `#8B5A2B` | Relleno de marco, botones secundarios |
| `WoodLight` | `#B07C3E` | Borde interior de marcos |
| `Parchment` | `#F4D9A0` | Fondo de panel principal |
| `ParchmentLight` | `#FCE8C2` | Fondo de retrato, texto sobre botones oscuros |
| `LabelRed` | `#C1440E` | Etiquetas de atributo, títulos |
| `AccentOrange` | `#E8930C` | Flechas, CTA primario, FAB |
| `ConfirmGreen` | `#4A7C3C` | Botón OK / Guardar |
| `InkBrown` | `#4A3220` | Texto de valor, texto de cuerpo |

No introduzcas colores Material (`#6200EE`, `#03DAC5`, azules/morados por defecto de
Compose) en ninguna pantalla. Si necesitas un color nuevo, añádelo a `PixelColors` con
nombre semántico, no lo escribas inline.

## Tipografía

El esqueleto actual usa la tipografía del sistema con pesos ajustados (`FontWeight.Bold`,
`ExtraBold`). Para un acabado más fiel:

1. Añade una fuente pixel para títulos y etiquetas — candidatas: "Press Start 2P" o
   "Silkscreen" (Google Fonts, licencia OFL, se pueden empaquetar como recurso de
   fuente en `composeApp/src/commonMain/composeResources/font/`).
2. Para el texto de valor (más largo, tipo "Camiseta oversize") usa una fuente pixel
   más legible en tamaños pequeños — "Pixelify Sans" es buena opción — para no
   sacrificar legibilidad por fidelidad estética.
3. Reserva la fuente pixel más extrema (Press Start 2P) para títulos cortos y números;
   úsala con moderación, no en párrafos largos.

## Layout

```
┌─────────────────────────────────────────────┐
│  [dado/icono]                          [×]   │  <- marco exterior
│  ┌──────────┐   Nueva prenda                 │
│  │          │   ◄ Categoría      👕 Top    ▶ │
│  │  FOTO    │   ◄ Tipo          Camiseta   ▶ │
│  │  PRENDA  │   ◄ Ajuste          Regular  ▶ │
│  │          │   ◄ Temporada  🌤️ Todo año   ▶ │
│  └──────────┘   ◄ Estilo           Casual   ▶ │
│   👕 Camiseta    Color                        │
│                  [========knob=========]      │
│                                                │
│         [ Cancelar ]        [    OK    ]      │
└─────────────────────────────────────────────┘
```

Esto ya está implementado en `AddGarmentScreen.kt`, incluida la adaptación a
pantallas estrechas mediante `BoxWithConstraints`. Al evolucionarlo, ten en cuenta:

- Mantén la variante vertical con foto arriba y atributos debajo en anchos estrechos.
- El marco de madera (`WoodFrame`) usa paddings fijos en `dp` que no escalan con
  `fontScale` del sistema — verifica accesibilidad con texto grande activado.

## Señal única del diseño (signature element)

El **marco de madera de doble borde con esquinas cortadas** (`CutCornerShape` en los
botones, bordes rectos en el marco) es el elemento que debe repetirse consistentemente
en todas las pantallas para que la app se sienta como un solo objeto de diseño y no
varias superficies distintas
pegadas con celo. Si añades pantallas nuevas (detalle de prenda, favoritos), reutiliza
`WoodFrame` como contenedor raíz siempre que la pantalla sea un "panel" flotante sobre
el fondo `WoodDark`; para pantallas de lista a pantalla completa (como `WardrobeScreen`)
el marco puede aplicarse solo al contenedor del grid, no a toda la pantalla.

## Qué NO hacer

- Nada de `elevation`/sombra difusa de Material — las superficies se diferencian por
  color y borde, no por sombra.
- Nada de esquinas muy redondeadas (`RoundedCornerShape` grande) — usa `CutCornerShape`
  pequeño o esquinas rectas.
- Nada de animaciones de "ripple" Material por defecto en los botones pixel — si se
  anima algo, que sea un cambio de color instantáneo o un desplazamiento de 1-2px al
  pulsar (como el "pressed" que ya simula `PixelArrowButton`), nunca una onda difusa.
