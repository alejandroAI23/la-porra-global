import { Link } from 'react-router-dom'

export default function Home() {
  return (
    <section className="text-center">
      <h1 className="text-4xl font-extrabold tracking-tight sm:text-5xl">
        Tu porra del <span className="text-pitch-500">gran torneo de 2026</span>
      </h1>
      <p className="mx-auto mt-4 max-w-2xl text-lg text-slate-400">
        Crea ligas privadas de predicciones con tus amigos, tu empresa o tu bar
        favorito. Acierta resultados, suma puntos y presume en el ranking.
      </p>
      <div className="mt-8 flex flex-wrap justify-center gap-3">
        <Link
          to="/partidos"
          className="rounded-lg bg-pitch-700 px-5 py-2.5 font-medium hover:bg-pitch-500"
        >
          Ver partidos
        </Link>
        <Link
          to="/ligas"
          className="rounded-lg border border-slate-600 px-5 py-2.5 font-medium hover:border-pitch-500"
        >
          Crear o unirse a una liga
        </Link>
      </div>
      <div className="mt-10 grid gap-6 sm:grid-cols-3">
        <div className="rounded-xl border border-slate-700 bg-slate-800/50 p-6">
          <h2 className="text-lg font-semibold">🎯 Predice</h2>
          <p className="mt-2 text-sm text-slate-400">
            Pronostica el resultado de cada partido antes del pitido inicial.
          </p>
        </div>
        <div className="rounded-xl border border-slate-700 bg-slate-800/50 p-6">
          <h2 className="text-lg font-semibold">🏆 Compite</h2>
          <p className="mt-2 text-sm text-slate-400">
            Ligas privadas con código de invitación para tu grupo.
          </p>
        </div>
        <div className="rounded-xl border border-slate-700 bg-slate-800/50 p-6">
          <h2 className="text-lg font-semibold">🍻 Comparte</h2>
          <p className="mt-2 text-sm text-slate-400">
            Encuentra bares con eventos para ver los partidos en grupo.
          </p>
        </div>
      </div>
    </section>
  )
}
