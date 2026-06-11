import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { api } from '../api/client.js'

const TYPE_LABELS = {
  FRIENDS: 'Amigos',
  COMPANY: 'Empresa',
  BAR: 'Bar',
}

export default function Leagues() {
  const navigate = useNavigate()
  const [createForm, setCreateForm] = useState({
    name: '',
    description: '',
    type: 'FRIENDS',
    maxMembers: 50,
  })
  const [joinForm, setJoinForm] = useState({ leagueId: '', inviteCode: '' })
  const [created, setCreated] = useState(null)
  const [error, setError] = useState(null)
  const [busy, setBusy] = useState(false)

  async function handleCreate(e) {
    e.preventDefault()
    setError(null)
    setBusy(true)
    try {
      const league = await api.createLeague({
        ...createForm,
        maxMembers: Number(createForm.maxMembers),
      })
      setCreated(league)
    } catch (e) {
      setError(e.message)
    } finally {
      setBusy(false)
    }
  }

  async function handleJoin(e) {
    e.preventDefault()
    setError(null)
    setBusy(true)
    try {
      await api.joinLeague(joinForm.leagueId, { inviteCode: joinForm.inviteCode })
      navigate(`/ligas/${joinForm.leagueId}`)
    } catch (e) {
      setError(e.message)
    } finally {
      setBusy(false)
    }
  }

  return (
    <section>
      <h1 className="text-2xl font-bold">Ligas privadas</h1>
      {error && <p className="mt-4 text-red-400">{error}</p>}

      <div className="mt-6 grid gap-6 md:grid-cols-2">
        <div className="rounded-xl border border-slate-700 bg-slate-800/50 p-6">
          <h2 className="text-lg font-semibold">Crear una liga</h2>
          <form onSubmit={handleCreate} className="mt-4 space-y-3">
            <input
              className="w-full rounded-lg border border-slate-700 bg-slate-900 px-4 py-2"
              placeholder="Nombre de la liga"
              value={createForm.name}
              onChange={(e) => setCreateForm({ ...createForm, name: e.target.value })}
              required
            />
            <input
              className="w-full rounded-lg border border-slate-700 bg-slate-900 px-4 py-2"
              placeholder="Descripción (opcional)"
              value={createForm.description}
              onChange={(e) =>
                setCreateForm({ ...createForm, description: e.target.value })
              }
            />
            <select
              className="w-full rounded-lg border border-slate-700 bg-slate-900 px-4 py-2"
              value={createForm.type}
              onChange={(e) => setCreateForm({ ...createForm, type: e.target.value })}
            >
              {Object.entries(TYPE_LABELS).map(([value, label]) => (
                <option key={value} value={value}>
                  {label}
                </option>
              ))}
            </select>
            <input
              type="number"
              min="2"
              className="w-full rounded-lg border border-slate-700 bg-slate-900 px-4 py-2"
              placeholder="Máximo de miembros"
              value={createForm.maxMembers}
              onChange={(e) =>
                setCreateForm({ ...createForm, maxMembers: e.target.value })
              }
            />
            <button
              disabled={busy}
              className="w-full rounded-lg bg-pitch-700 py-2 font-medium hover:bg-pitch-500 disabled:opacity-50"
            >
              Crear liga
            </button>
          </form>
          {created && (
            <div className="mt-4 rounded-lg border border-pitch-700 bg-pitch-900/30 p-4 text-sm">
              <p className="font-semibold">¡Liga «{created.name}» creada!</p>
              <p className="mt-1 text-slate-300">
                ID: <span className="font-mono">{created.id}</span>
              </p>
              <p className="text-slate-300">
                Código de invitación:{' '}
                <span className="font-mono text-pitch-400">{created.inviteCode}</span>
              </p>
              <button
                onClick={() => navigate(`/ligas/${created.id}`)}
                className="mt-3 rounded-lg border border-slate-600 px-3 py-1 text-xs hover:border-pitch-500"
              >
                Ver ranking
              </button>
            </div>
          )}
        </div>

        <div className="rounded-xl border border-slate-700 bg-slate-800/50 p-6">
          <h2 className="text-lg font-semibold">Unirse a una liga</h2>
          <form onSubmit={handleJoin} className="mt-4 space-y-3">
            <input
              type="number"
              className="w-full rounded-lg border border-slate-700 bg-slate-900 px-4 py-2"
              placeholder="ID de la liga"
              value={joinForm.leagueId}
              onChange={(e) => setJoinForm({ ...joinForm, leagueId: e.target.value })}
              required
            />
            <input
              className="w-full rounded-lg border border-slate-700 bg-slate-900 px-4 py-2 font-mono uppercase"
              placeholder="Código de invitación"
              value={joinForm.inviteCode}
              onChange={(e) =>
                setJoinForm({ ...joinForm, inviteCode: e.target.value.toUpperCase() })
              }
              required
            />
            <button
              disabled={busy}
              className="w-full rounded-lg bg-pitch-700 py-2 font-medium hover:bg-pitch-500 disabled:opacity-50"
            >
              Unirme
            </button>
          </form>
        </div>
      </div>
    </section>
  )
}
