import { useEffect, useState } from 'react'
import { api, isAuthenticated } from '../api/client.js'

const STATUS_LABELS = {
  SCHEDULED: 'Programado',
  LIVE: 'En juego',
  FINISHED: 'Finalizado',
  POSTPONED: 'Aplazado',
  CANCELLED: 'Cancelado',
}

function MatchCard({ match, onPredicted }) {
  const [home, setHome] = useState('')
  const [away, setAway] = useState('')
  const [msg, setMsg] = useState(null)
  const [err, setErr] = useState(null)
  const [suggestion, setSuggestion] = useState(null)
  const [busy, setBusy] = useState(false)

  const open = match.status === 'SCHEDULED'

  async function submit(e) {
    e.preventDefault()
    setErr(null)
    setMsg(null)
    setBusy(true)
    try {
      await api.createPrediction({
        matchId: match.id,
        homeScore: Number(home),
        awayScore: Number(away),
      })
      setMsg('¡Predicción guardada!')
      onPredicted?.()
    } catch (e) {
      setErr(e.message)
    } finally {
      setBusy(false)
    }
  }

  async function suggest() {
    setErr(null)
    try {
      const s = await api.getSuggestion(match.id)
      setSuggestion(s)
      setHome(String(s.suggestedHomeScore))
      setAway(String(s.suggestedAwayScore))
    } catch (e) {
      setErr(e.message)
    }
  }

  return (
    <div className="rounded-xl border border-slate-700 bg-slate-800/50 p-5">
      <div className="flex items-center justify-between text-xs text-slate-400">
        <span>{new Date(match.kickoffAt).toLocaleString('es-ES')}</span>
        <span className="rounded-full bg-slate-700 px-2 py-0.5">
          {STATUS_LABELS[match.status] ?? match.status}
        </span>
      </div>
      <div className="mt-3 flex items-center justify-center gap-3 text-lg font-semibold">
        <span>{match.homeTeam.flagEmoji} {match.homeTeam.name}</span>
        <span className="text-slate-500">
          {match.status === 'FINISHED'
            ? `${match.homeScore} - ${match.awayScore}`
            : 'vs'}
        </span>
        <span>{match.awayTeam.name} {match.awayTeam.flagEmoji}</span>
      </div>

      {isAuthenticated() && open && (
        <form onSubmit={submit} className="mt-4 flex flex-wrap items-center justify-center gap-2">
          <input
            type="number"
            min="0"
            className="w-16 rounded-lg border border-slate-700 bg-slate-900 px-2 py-1 text-center"
            value={home}
            onChange={(e) => setHome(e.target.value)}
            required
          />
          <span className="text-slate-500">-</span>
          <input
            type="number"
            min="0"
            className="w-16 rounded-lg border border-slate-700 bg-slate-900 px-2 py-1 text-center"
            value={away}
            onChange={(e) => setAway(e.target.value)}
            required
          />
          <button
            disabled={busy}
            className="rounded-lg bg-pitch-700 px-3 py-1 text-sm font-medium hover:bg-pitch-500 disabled:opacity-50"
          >
            Predecir
          </button>
          <button
            type="button"
            onClick={suggest}
            className="rounded-lg border border-slate-600 px-3 py-1 text-sm hover:border-pitch-500"
          >
            🤖 Sugerir
          </button>
        </form>
      )}

      {suggestion && (
        <p className="mt-2 text-center text-xs text-slate-400">
          Sugerencia ({suggestion.confidence}): {suggestion.rationale}
        </p>
      )}
      {msg && <p className="mt-2 text-center text-xs text-pitch-400">{msg}</p>}
      {err && <p className="mt-2 text-center text-xs text-red-400">{err}</p>}
      {!isAuthenticated() && open && (
        <p className="mt-3 text-center text-xs text-slate-500">
          Inicia sesión para predecir este partido.
        </p>
      )}
    </div>
  )
}

export default function Matches() {
  const [matches, setMatches] = useState([])
  const [error, setError] = useState(null)
  const [loading, setLoading] = useState(true)

  async function load() {
    setLoading(true)
    try {
      setMatches(await api.getMatches())
    } catch (e) {
      setError(e.message)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    load()
  }, [])

  return (
    <section>
      <h1 className="text-2xl font-bold">Partidos</h1>
      <p className="mt-1 text-sm text-slate-400">
        Predice el marcador antes del pitido inicial. Las predicciones se cierran al
        comenzar el partido.
      </p>
      {loading && <p className="mt-6 text-slate-400">Cargando…</p>}
      {error && <p className="mt-6 text-red-400">{error}</p>}
      <div className="mt-6 grid gap-4 sm:grid-cols-2">
        {matches.map((m) => (
          <MatchCard key={m.id} match={m} />
        ))}
      </div>
    </section>
  )
}
