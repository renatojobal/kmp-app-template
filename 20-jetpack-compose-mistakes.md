# 20 Things You Should NEVER Do in Jetpack Compose

> Por Philipp Lackner – Drop Table
>
> Esta lista es para todos los desarrolladores móviles que usan Jetpack Compose para construir la UI de sus apps. Úsala para aprender los errores a evitar, o como checklist para revisar tus proyectos existentes.
>
> **Nota:** Los snippets de código mostrados aquí solo se enfocan en el error de cada caso y no pretenden evitar todos los demás errores simultáneamente.

---

## 1. Calling Non-Compose Code in Composable Functions

Cada vez que un composable es recompuesto (es decir, un valor de estado que usa cambia), la función composable se vuelve a llamar. En general, las funciones composables pueden llamarse en cualquier momento y en cualquier orden.

Si pones código no-Compose dentro de una función composable (llamar a una función sin `@Composable` dentro de una con `@Composable`), esa función podría ejecutarse muchísimas veces.

### ❌ Bad

```kotlin
@Composable
fun BookingList() {
    val scope = rememberCoroutineScope()
    var bookings by remember {
        mutableStateOf<List<Booking>>(emptyList())
    }

    scope.launch {
        bookings = loadBookings()
    }

    LazyColumn {
        items(bookings) {
            // ...
        }
    }
}
```

Cada vez que `BookingList` es recompuesto, lanzará una nueva coroutine y cargará los bookings con una llamada de red de larga duración. Terrible.

### ✅ Good

```kotlin
@Composable
fun BookingList() {
    var bookings by remember {
        mutableStateOf<List<Booking>>(emptyList())
    }
    LaunchedEffect(true) {
        bookings = loadBookings()
    }

    LazyColumn {
        items(bookings) {
            // ...
        }
    }
}
```

Usa los **effect handlers** de Jetpack Compose como `LaunchedEffect`, `DisposableEffect`, etc.

---

## 2. Using MutableList as a State

Malentender cómo funciona el estado en Compose puede generar muchos bugs y comportamiento inesperado.

### ❌ Bad

```kotlin
@Composable
fun NamesList() {
    val names by remember {
        mutableStateOf(mutableListOf<String>())
    }

    LazyColumn {
        item {
            Button(onClick = { names.add("Hans") }) {
                Text(text = "Add name")
            }
        }
        items(names) { name ->
            Text(name)
        }
    }
}
```

Al hacer click en el botón, se agrega un nombre a la lista. Sin embargo, la lista no será recompuesta, ya que Compose **no puede detectar cambios en tipos de datos mutables** como `MutableList`.

### ✅ Good

```kotlin
@Composable
fun NamesList() {
    var names by remember {
        mutableStateOf(listOf<String>())
    }

    LazyColumn {
        item {
            Button(onClick = {
                names = names + "Hans"
            }) {
                Text(text = "Add name")
            }
        }
        items(names) { name ->
            Text(name)
        }
    }
}
```

En cambio, usa una **lista inmutable**. Cuando el estado cambia y se reemplaza por un nuevo valor, Compose detecta el cambio y recompone los composables que usan ese estado.

---

## 3. Creating State with `remember`

Una forma muy común de crear estado en Compose es usando `remember`. Eso es correcto, ya que no recreará el estado en cada recomposición. Pero en apps reales siempre debes pensar si esto puede salir mal, ya que `remember` solo cachea el valor entre recomposiciones **mientras no haya un configuration change o muerte del proceso**.

### ❌ Bad

```kotlin
@Composable
fun LoginScreen() {
    var emailText by remember {
        mutableStateOf("")
    }

    TextField(
        value = emailText,
        onValueChange = { emailText = it }
    )
}
```

Si el usuario rota la pantalla, el estado `emailText` se reseteará a un string vacío y el text field quedará vacío. ¡Muy frustrante para el usuario!

### ✅ Good

```kotlin
class LoginViewModel(
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    val emailText by savedStateHandle.saveable("emailText") {
        mutableStateOf("")
    }

    fun onEmailTextChange(value: String) {
        savedStateHandle["emailText"] = value
    }
}
```

Recomendamos guardar tu estado en **ViewModels**, o si realmente quieres tener el estado dentro de tus composables, usar `rememberSaveable` que sobrevive configuration changes.

Dentro de tu ViewModel, puedes usar `SavedStateHandle` para restaurar el estado después de la muerte del proceso.

```kotlin
@Composable
fun AppRoot() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            val viewModel = viewModel<LoginViewModel>()
            LoginScreen(
                emailText = viewModel.emailText,
                onEmailTextChange = viewModel::onEmailTextChange
            )
        }
    }
}

@Composable
fun LoginScreen(
    emailText: String,
    onEmailTextChange: (String) -> Unit
) {
    TextField(
        value = emailText,
        onValueChange = onEmailTextChange
    )
    // ...
}
```

---

## 4. Not Using Keys Inside a LazyColumn

Cuando una lista usada en un `LazyColumn` se actualiza, el `LazyColumn` no sabe qué ítems cambiaron, por lo que actualizará y recompondrá todos los visibles.

### ❌ Bad

```kotlin
@Composable
fun NoteList(notes: List<Note>) {
    LazyColumn {
        items(notes) { note ->
            // ...
        }
    }
}
```

Cuando las notas cambian, cada composable visible en el `LazyColumn` será recompuesto.

### ✅ Good

```kotlin
@Composable
fun NoteList(notes: List<Note>) {
    LazyColumn {
        items(
            items = notes,
            key = { note ->
                note.id
            }
        ) { note ->
            // ...
        }
    }
}
```

Usa el lambda `key` para decirle al `LazyColumn` cómo identificar de forma única cada ítem (por ejemplo, por el ID de la nota). Así, el `LazyColumn` solo recompondrá los ítems que realmente cambiaron.

**Bonus:** También puedes animar fácilmente los cambios en la lista con el modifier `animateItemPlacement()`.

---

## 5. Using Unstable Classes from External Modules

Compose tiene el concepto de estabilidad e inestabilidad. En resumen, una clase es marcada como **estable** por el compilador de Compose si se cumplen todas estas condiciones:

1. El resultado de `equals()` siempre retorna el mismo resultado para dos instancias.
2. Cuando una propiedad pública del tipo cambia, la composición es notificada.
3. Todos los tipos de propiedades públicas son estables.

Lo que muchos no saben: **si usas clases de módulos externos o librerías que no usan Compose, esas clases son inestables por defecto.**

### ❌ Bad

```kotlin
// Módulo externo sin Compose
data class User(
    val id: String,
    val name: String,
    val isAdmin: Boolean,
    val profilePictureUrl: String
)

@Composable
fun UserProfile(user: User) {
    Column {
        ProfilePicture(user.profilePictureUrl)
        Text(text = user.name)

        if(user.isAdmin) {
            Text(text = "ADMIN")
        }
    }
}
```

Dado que las clases de módulos externos no-Compose son inestables por defecto, todos los composables que usen esa instancia serán recompuestos con cada cambio de cualquier campo del modelo.

### ✅ Good

```kotlin
// Mapper
fun User.toComposeUser(): ComposeUser {
    return ComposeUser(id, name, isAdmin, profilePictureUrl)
}

fun ComposeUser.toUser(): User {
    return User(id, name, isAdmin, profilePictureUrl)
}

@Composable
fun UserProfile(user: ComposeUser) { ... }
```

Crea un mapper que convierta el modelo de la librería a un modelo dentro de tu módulo Compose, ya que ese sí será considerado estable (porque el módulo usa Compose).

---

## 6. Consuming Flows with `collectAsState()`

En Compose, podemos transformar un `Flow` en un estado de Compose con la función `collectAsState()`. Sin embargo, es mejor evitar esta función en proyectos Android, ya que **no sabe nada sobre el ciclo de vida de tu Activity**. Eso significa que cuando tu Activity va al fondo, el `Flow` subyacente seguirá ejecutándose, aunque el usuario no vea los cambios en la UI.

> **Nota:** Esto no afecta a los `StateFlows` creados con `asStateFlow()`, sino solo a los creados con `stateIn()`.

### ❌ Bad

```kotlin
class CounterViewModel: ViewModel() {
    private val _counter = MutableStateFlow(0)
    val counter = _counter
        .onEach {
            saveCounterToDb(it)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
}

@Composable
fun Counter() {
    val viewModel = viewModel<CounterViewModel>()
    val counter by viewModel.counter.collectAsState()

    Text(text = counter.toString())
}
```

Incluso si la app está en segundo plano, el `Flow` del contador se ejecutará y realizará operaciones en la base de datos.

### ✅ Good

```kotlin
// Agrega esta dependencia en build.gradle
// implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.1")

@Composable
fun Counter() {
    val viewModel = viewModel<CounterViewModel>()
    val counter by viewModel.counter.collectAsStateWithLifecycle()

    Text(text = counter.toString())
}
```

Usa `collectAsStateWithLifecycle()` para obtener un colector de `Flow` que tenga en cuenta el ciclo de vida y que **no se ejecute cuando la app está en segundo plano**.

---

## 7. Animating Transform Outside of `graphicsLayer`

Para animaciones de transformación (rotación, escala, posición) en Compose, hay múltiples formas de hacerlo. Puedes usar los modifiers de transformación directamente o el modifier `graphicsLayer`.

### ❌ Bad

```kotlin
@Composable
fun RotatingBox() {
    val transition = rememberInfiniteTransition()
    val rotationRatio by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000),
        )
    )
    Box(modifier = Modifier
            .rotate(rotationRatio * 360f)
            .size(100.dp)
            .background(Color.Red))
}
```

Cada vez que `rotationRatio` cambia (muchas veces por segundo), el composable `Box` será recompuesto.

### ✅ Good

```kotlin
@Composable
fun RotatingBox() {
    val transition = rememberInfiniteTransition()
    val rotationRatio by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000),
        )
    )
    Box(modifier = Modifier
        // This is better
        .graphicsLayer {
            rotationZ = rotationRatio * 360f
        }
        .size(100.dp)
        .background(Color.Red))
}
```

Los composables no deberían recomponerse si su apariencia no cambia. El composable se verá exactamente igual después de rotar, solo está un poco girado. Por eso, usa el modifier `graphicsLayer`.

> **Regla:** Si cambia clipping, transform o alpha → usa `graphicsLayer`.

---

## 8. Creating ViewModels on Screen Level with Hilt

### ❌ Bad

```kotlin
@Composable
fun LoginScreen() {
    val viewModel: LoginViewModel = hiltViewModel()
    val state = viewModel.state
    Column {
        if(state.isLoading) {
            CircularProgressIndicator()
        }
        Button(onClick = { viewModel.login() }) {
            Text(text = "Login")
        }
    }
}
```

En cuanto tu ViewModel tenga dependencias inyectadas en su constructor, esto romperá el preview y los tests de UI aislados, ya que esta pantalla no se puede usar de forma independiente.

### ✅ Good

```kotlin
sealed interface LoginEvent {
    object Login: LoginEvent
    // More events...
}

class LoginViewModel: ViewModel() {
    var state by mutableStateOf(LoginState())
        private set

    fun onEvent(event: LoginEvent) {
        when(event) {
            is LoginEvent.Login -> { /* Handle login */ }
            // ...
        }
    }
}

@Composable
fun LoginScreen(
    state: LoginState,
    onEvent: (LoginEvent) -> Unit
) {
    Column {
        if(state.isLoading) {
            CircularProgressIndicator()
        }
        Button(onClick = { onEvent(LoginEvent.Login) }) {
            Text(text = "Login")
            // ...
        }
    }
}

@Composable
fun LoginScreenRoot() {
    val viewModel = hiltViewModel<LoginViewModel>()
    LoginScreen(
        state = viewModel.state,
        onEvent = viewModel::onEvent
    )
}
```

Recomendamos estructurar los ViewModels así: cada ViewModel contiene una instancia de estado de pantalla y expone una función `onEvent`. Luego, en lugar de pasar la instancia del ViewModel a la pantalla, solo pasas el estado y un lambda `onEvent`, pudiendo instanciar la pantalla fácilmente para previews y tests.

---

## 9. Setting Expanding Sizes in Sub-Composables

Un objetivo importante de Compose es hacer tus componentes lo más reutilizables posible.

### ❌ Bad

```kotlin
@Composable
fun MyButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .clip(RoundedCornerShape(100))
            .fillMaxWidth()
    ) {
        Text(text = "Cool button")
    }
}
```

El modifier `fillMaxWidth()` fuerza a que cada instancia del botón siempre llene todo el ancho del composable padre, impidiendo poner dos botones uno al lado del otro sin workarounds.

### ✅ Good

```kotlin
@Composable
fun MyButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .clip(RoundedCornerShape(100))
    ) {
        Text(text = "Cool button")
    }
}

// Uso:
MyButton(
    onClick = { /*TODO*/ },
    modifier = Modifier
        .fillMaxWidth() // <- mejor aquí
)
```

No hardcodees tamaños expandibles en los modifiers raíz de un composable reutilizable. Pásalos desde afuera para mantener flexibilidad.

---

## 10. Not Using `remember` for Heavy Computations

La función `remember` puede usarse para cachear un valor computado entre recomposiciones.

### ❌ Bad

```kotlin
@Composable
fun EncryptedImage(
    encryptedBytes: ByteArray,
    modifier: Modifier = Modifier
) {
    val decryptedBytes = CryptoManager.decrypt(encryptedBytes)
    val bitmap = BitmapFactory.decodeByteArray(decryptedBytes, 0, decryptedBytes.size)
    Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = null,
        modifier = modifier
    )
}
```

Los bytes serán descifrados en **cada recomposición**, lo cual es muy costoso computacionalmente.

### ✅ Good

```kotlin
@Composable
fun EncryptedImage(
    encryptedBytes: ByteArray,
    modifier: Modifier = Modifier
) {
    val bitmap = remember(encryptedBytes) {
        val decryptedBytes = CryptoManager.decrypt(encryptedBytes)
        BitmapFactory.decodeByteArray(decryptedBytes, 0, decryptedBytes.size)
    }
    Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = null,
        modifier = modifier
    )
}
```

Usa `remember` con una key para que solo se recalcule cuando la key cambie. (Esto también aplica a cálculos pesados relacionados con la UI, como evaluar condiciones complejas.)

---

## 11. Overusing Hardcoded DP Units

Evita usar valores DP hardcodeados para las dimensiones de un composable. Usa tamaños relativos con modifiers como `fillMaxSize()`, `weight()` o `widthIn()`.

### ❌ Bad

```kotlin
@Composable
fun LoginScreen(state: LoginState) {
    Column {
        TextField(
            value = state.email,
            onValueChange = { /* ... */ },
            modifier = Modifier.width(300.dp)
        )
    }
}
```

Este text field tendrá un ancho de `300dp` en **todos** los tamaños de pantalla. Solo porque se ve bien en tu dispositivo de prueba no significa que se verá bien en dispositivos más pequeños, más grandes o tablets.

### ✅ Good

```kotlin
@Composable
fun LoginScreen(state: LoginState) {
    Column {
        TextField(
            value = state.email,
            onValueChange = { /* ... */ },
            modifier = Modifier
                .widthIn(max = 400.dp)
                .fillMaxWidth()
        )
    }
}
```

Este text field llenará todo el ancho en teléfonos, pero en tablets tendrá un tamaño fijo de `400dp`.

---

## 12. Forgetting About Touch Target Size

Al crear composables clickeables, hay que tener en cuenta el tamaño del área de toque. Si tu composable es pequeño, puede ser difícil para el usuario hacer click en él.

### ❌ Bad

```kotlin
@Composable
fun OptionsButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = Icons.Default.Menu,
        contentDescription = "Options",
        modifier = modifier
            .clickable { onClick() }
    )
}
```

Un ícono generalmente es más pequeño que un dedo, por lo que hacer click en él puede ser frustrante.

### ✅ Good

```kotlin
@Composable
fun OptionsButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "Options",
        )
    }
}
```

Usa `IconButton`, ya que ya se encarga de tener un área de toque suficientemente grande. Esto aplica a cualquier composable clickeable pequeño.

---

## 13. Not Checking View Decomposition Strategy in Fragments

Compose ofrece gran interoperabilidad con XML. Sin embargo, al usar un `ComposeView` dentro de un `Fragment`, asegúrate de establecer la estrategia de descomposición de vista correcta.

Por defecto, la composición se descarta cuando el `ComposeView` subyacente se desacopla de la ventana (deseado para apps pure Compose). Sin embargo, al agregar Compose de forma incremental, esto puede no ser lo que quieres.

### ❌ Bad

```kotlin
class LoginFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                // Composable screen
            }
        }
    }
}
```

Usar un `ComposeView` así no garantiza que la composición esté completamente ligada al ciclo de vida del `Fragment`, lo que puede llevar a pérdida de estado.

### ✅ Good

```kotlin
return ComposeView(requireContext()).apply {
    setViewCompositionStrategy(
        ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
    )
    setContent {
        // Composable screen
    }
}
```

> **Nota:** Esto solo es necesario en Fragments.

---

## 14. Mixing State Naming

En desarrollo de UI, hay 2 formas de crear y nombrar tu estado:

1. Lo nombras según el impacto que tendrá en la UI.
2. Lo nombras según el comportamiento lógico que representa.

Cualquier enfoque es válido, pero **es importante ser consistente** con uno solo.

### ❌ Bad

```kotlin
data class LoginState(
    val emailText: String = "",
    val isProgressBarVisible: Boolean = false,  // nombrado según impacto en UI
    val isLoginFailed: Boolean = false           // nombrado según comportamiento lógico
)
```

`isProgressBarVisible` describe exactamente el impacto en la UI, mientras que `isLoginFailed` describe un comportamiento lógico. Nomenclatura inconsistente.

### ✅ Good

```kotlin
data class LoginState(
    val emailText: String = "",
    val isLoggingIn: Boolean = false,    // renombrado para reflejar comportamiento
    val isLoginFailed: Boolean = false
)
```

Así cada composable puede decidir por sí mismo qué hacer con ese estado.

---

## 15. Forgetting About Making Columns Scrollable

A veces tu pantalla tiene muchos composables en un `Column` normal. Aunque sea un número estático con tamaño fijo, deberías considerar hacer la pantalla scrollable. El hecho de que todo entre en tu dispositivo de prueba no significa que entrará en dispositivos más pequeños.

### ❌ Bad

```kotlin
@Composable
fun AgendaScreen() {
    Column {
        // Lots of composables
    }
}
```

### ✅ Good

```kotlin
@Composable
fun AgendaScreen() {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        // Lots of composables
    }
}
```

No haces nada malo al hacer el contenido scrollable. Considera agregar el modifier `verticalScroll()` a los `Column` externos de una pantalla.

---

## 16. Not Naming Lambdas in Composables

Los lambdas juegan un papel importante en Jetpack Compose para reaccionar a acciones del usuario y levantar estado. Sin embargo, la feature de Kotlin de poner el último lambda de los parámetros de una función fuera de los paréntesis puede hacer tu código ilegible.

### ❌ Bad

```kotlin
@Composable
fun LoginScreen() {
    VerticalScrollContainer(
        content = {
            // ...
        }
    ) {
        // ¿Cuándo se llama este lambda y para qué sirve?
    }
}
```

### ✅ Good

```kotlin
@Composable
fun LoginScreen() {
    VerticalScrollContainer(
        content = {
            // ...
        },
        onScroll = { scrollOffset ->
            // Ahora está claro para qué sirve :)
        }
    )
}
```

Si un lambda se usa para colocar contenido composable, suele quedar claro como trailing lambda. Si se usa como callback, mejor usa parámetros nombrados.

---

## 17. Misusing `rememberCoroutineScope`

Con `rememberCoroutineScope()`, obtenemos un scope de coroutine que tiene conciencia de la composición actual. Sin embargo, muchos lo usan de la forma incorrecta.

### ❌ Bad

```kotlin
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    navController: NavController
) {
    val scope = rememberCoroutineScope()
    Button(onClick = {
        scope.launch {
            val result = viewModel.login() // Suspending function
            if(result == Result.SUCCESS) {
                navController.navigate("home")
            }
        }
    }) {
        Text(text = "Login")
    }
}
```

No uses el scope de coroutine del composable para ejecutar funciones suspend que no están relacionadas con la UI. Tu ViewModel no debería exponer funciones suspend a la UI, ya que este scope será cancelado después de un configuration change como una rotación de pantalla (y la llamada de login se cancelaría también).

### ✅ Good

```kotlin
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    navController: NavController
) {
    LaunchedEffect(key1 = viewModel.isLoggedIn) {
        if(viewModel.isLoggedIn) {
            navController.navigate("home") {
                popUpTo("home") {
                    inclusive = false
                }
            }
        }
    }
    Button(onClick = {
        viewModel.login() // Not suspending
    }) {
        Text(text = "Login")
    }
}
```

En su lugar, lanza dichas funciones dentro de un `viewModelScope` coroutine y actualiza un estado cuando la llamada suspendida termine.

> **Regla:** Solo usa el scope de coroutine del composable para funciones suspend relacionadas con la UI, como disparar una animación o mostrar un snackbar.

---

## 18. Frequently Changing State in Sub-Composable and `graphicsLayer`

Como aprendimos en el punto 7, el estado que afecta la transformación, clipping o alpha de un composable no debería necesitar una recomposición, ya que podemos usar el modifier `graphicsLayer`. Sin embargo, si tal estado frecuentemente cambiante se pasa a un sub-composable, aún puede causar muchas recomposiciones no deseadas.

### ❌ Bad

```kotlin
@Composable
fun ListScreen() {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        for(i in 1..50) {
            ListItem(
                alpha = scrollState.value / 50f,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun ListItem(
    alpha: Float,
    modifier: Modifier = Modifier
) {
    Text(
        text = "List item",
        modifier = modifier
            .padding(32.dp)
            .graphicsLayer {
                this.alpha = alpha
            }
    )
}
```

Cada vez que el usuario hace scroll, `scrollState` cambia y dispara una recomposición de `ListItem`.

### ✅ Good

```kotlin
@Composable
fun ListScreen() {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        for(i in 1..50) {
            ListItem(
                alpha = { scrollState.value / 50f },  // Lambda, no valor directo
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun ListItem(
    alpha: () -> Float,  // Lambda
    modifier: Modifier = Modifier
) {
    Text(
        text = "List item",
        modifier = modifier
            .padding(32.dp)
            .graphicsLayer {
                this.alpha = alpha()
            }
    )
}
```

Si el estado se pasa como resultado de un lambda, el composable `ListItem` puede saltar la fase de composición e ir directo a la fase de layout, ya que el lambda en sí no cambia y no disparará una recomposición.

---

## 19. Not Using Content Padding of Scaffold

Un `Scaffold` es un layout que ayuda a colocar correctamente los componentes comunes de la UI de Android (navigation drawers, snackbars, toolbars). Un error común es ignorar el parámetro `contentPadding` del `Scaffold`.

### ❌ Bad

```kotlin
@Composable
fun LoginScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Screen content
        }
    }
}
```

Cuando el `Scaffold` contiene elementos como un `BottomAppBar`, esto reducirá el espacio restante. El contenido puede quedar oculto detrás de los componentes del Scaffold.

### ✅ Good

```kotlin
@Composable
fun LoginScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Screen content
        }
    }
}
```

Al aplicar `paddingValues` al composable `Box`, te aseguras de que ningún contenido quede oculto detrás de los composables del Scaffold.

---

## 20. Returning in Composable Functions

Evita usar la keyword `return` en funciones composables, ya que puede llevar a comportamiento indefinido cuando la fase de composición es saltada.

### ❌ Bad

```kotlin
@Composable
fun LoginScreen(state: LoginState) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        EmailTextField(/* ... */)
        PasswordTextField(/* ... */)
        Text(
            text = state.loginError ?: return@Column,
        )
    }
}
```

No uses `return` de ninguna forma dentro de una función anotada con `@Composable`.

### ✅ Good

```kotlin
@Composable
fun LoginScreen(state: LoginState) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        EmailTextField(/* ... */)
        PasswordTextField(/* ... */)
        state.loginError?.let { error ->
            Text(
                text = error
            )
        }
    }
}
```

Mejor usa `let` o sentencias `if` para null checks.

---

## Resumen Rápido

| # | Mistake | Fix |
|---|---------|-----|
| 1 | Calling non-Compose code in composables | Usar `LaunchedEffect`, `DisposableEffect` |
| 2 | Using `MutableList` as state | Usar listas inmutables |
| 3 | Using `remember` without considering config changes | Usar `rememberSaveable` o ViewModels |
| 4 | No keys in `LazyColumn` | Agregar `key` lambda |
| 5 | Unstable classes from external modules | Crear mappers a modelos internos |
| 6 | `collectAsState()` sin lifecycle | Usar `collectAsStateWithLifecycle()` |
| 7 | Transform animations sin `graphicsLayer` | Usar `graphicsLayer` modifier |
| 8 | ViewModels en screen level con Hilt | Separar screen root del screen composable |
| 9 | Expanding sizes en sub-composables | Pasar modifiers desde afuera |
| 10 | No cachear cálculos pesados | Usar `remember` con key |
| 11 | Hardcoded DP values | Usar `fillMaxWidth()`, `widthIn()`, `weight()` |
| 12 | Ignorar touch target size | Usar `IconButton` y componentes adecuados |
| 13 | Sin view decomposition strategy en Fragments | Usar `DisposeOnViewTreeLifecycleDestroyed` |
| 14 | Inconsistencia en nombres de estado | Elegir un approach y ser consistente |
| 15 | Columns no scrollables | Agregar `verticalScroll()` modifier |
| 16 | Lambdas sin nombre | Usar parámetros nombrados para callbacks |
| 17 | Misuse de `rememberCoroutineScope` | Usar `viewModelScope` para lógica de negocio |
| 18 | Estado frecuente en sub-composables | Pasar lambdas en lugar de valores directos |
| 19 | Ignorar `contentPadding` de Scaffold | Aplicar `paddingValues` al contenido |
| 20 | `return` en funciones composables | Usar `let` o `if` para null checks |

---

*Fuente: Philipp Lackner – Drop Table | [pl-coding.com/premium-courses](https://pl-coding.com/premium-courses)*
