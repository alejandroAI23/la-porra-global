import { useEffect, useState } from 'react'
import { api, isAuthenticated } from '../api/client.js'

function BarEvents({ barId }) {
  const [events, setEvents] = useState(null)

  async function load() {
    try {
      setEvents(await api.getBarEvents(barId))
    } catch {
      setEvents([])
    }
  }

  if (events === null) {
    return (
      <button
        onClick={load}
        className="mt-3 text-xs text-pitch-400 hover:underline"
      >
        Ver eventos
      </button>
    )
  }

  return (
    <ul className="mt-3 space-y-1 text-xs text-slate-300">
      {events.length === 0 && <li className="text-slate-500">Sin eventos.</li>}
      {events.map((ev) => (
        <li key={ev.id}>
          📅 {ev.title} · {new Date(ev.startsAt).toLocaleString('es-ES')}
        </li>
      ))}
    </ul>
  )
}

export default function Bars() {
  const [bars, setBars] = useState([])
  const [error, setError] = useState(null)
  const [loading, setLoading] = useState(true)
  const [form, setForm] = useState({ name: '', city: '', address: '', description: '' })
  const [creating, setCreating] = useState(false)

  async function load() {
    setLoading(true)
    try {
      setBars(await api.getBars())
    } catch (e) {
      setError(e.message)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    load()
  }, [])

  async function handleCreate(e) {
    e.preventDefault()
    setError(null)
    setCreating(true)
    try {
      await api.createBar(form)
      setForm({ name: '', city: '', address: '', description: '' })
      await load()
    } catch (e) {
      setError(e.message)
    } finally {
      setCreating(false)
    }
  }

  return (
    <section>
      <h1 className="text-2xl font-bold">Bares</h1>
      <p className="mt-1 text-sm text-slate-400">
        Encuentra locales con eventos para ver los partidos en grupo.
      </p>
      {error && <p className="mt-4 text-red-400">{error}</p>}

      {isAuthenticated() && (
        <form
          onSubmit={handleCreate}
          className="mt-6 grid gap-3 rounded-xl border border-slate-700 bg-slate-800/50 p-5 sm:grid-cols-2"
        >
          <input
            className="rounded-lg border border-slate-700 bg-slate-900 px-4 py-2"
            placeholder="Nombre del bar"
            value={form.name}
            onChange={(e) => setForm({ ...form, name: e.target.value })}
            required
          />
          <input
            className="rounded-lg border border-slate-700 bg-slate-900 px-4 py-2"
            placeholder="Ciudad"
            value={form.city}
            onChange={(e) => setForm({ ...form, city: e.target.value })}
            required
          />
          <input
            className="rounded-lg border border-slate-700 bg-slate-900 px-4 py-2 sm:col-span-2"
            placeholder="Dirección (opcional)"
            value={form.address}
            onChange={(e) => setForm({ ...form, address: e.target.value })}
          />
          <input
            className="rounded-lg border border-slate-700 bg-slate-900 px-4 py-2 sm:col-span-2"
            placeholder="Descripción (opcional)"
            value={form.description}
            onChange={(e) => setForm({ ...form, description: e.target.value })}
          />
          <button
            disabled={creating}
            className="rounded-lg bg-pitch-700 py-2 font-medium hover:bg-pitch-500 disabled:opacity-50 sm:col-span-2"
          >
            Registrar bar
          </button>
        </form>
      )}

      {loading && <p className="mt-6 text-slate-400">Cargando…</p>}
      <div className="mt-6 grid gap-4 sm:grid-cols-2">
        {bars.map((bar) => (
          <div
            key={bar.id}
            className="rounded-xl border border-slate-700 bg-slate-800/50 p-5"
          >
            <div className="flex items-center justify-between">
              <h2 className="text-lg font-semibold">{bar.name}</h2>
              {bar.verified && (
                <span className="rounded-full bg-pitch-700 px-2 py-0.5 text-xs">
                  Verificado
                </span>
              )}
            </div>
            <p className="text-sm text-slate-400">{bar.city}</p>
            {bar.description && (
              <p className="mt-2 text-sm text-slate-300">{bar.description}</p>
            )}
            <BarEvents barId={bar.id} />
          </div>
        ))}
        {!loading && bars.length === 0 && (
          <p className="text-slate-500">Todavía no hay bares registrados.</p>
        )}
      </div>
    </section>
  )
}
