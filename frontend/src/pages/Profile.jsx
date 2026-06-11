import { useEffect, useState } from 'react'
import { api } from '../api/client.js'

export default function Profile() {
  const [me, setMe] = useState(null)
  const [subscription, setSubscription] = useState(null)
  const [plans, setPlans] = useState([])
  const [predictions, setPredictions] = useState([])
  const [error, setError] = useState(null)
  const [msg, setMsg] = useState(null)
  const [displayName, setDisplayName] = useState('')

  async function load() {
    setError(null)
    try {
      const [meData, sub, planList, preds] = await Promise.all([
        api.getMe(),
        api.getSubscription(),
        api.getPlans(),
        api.getMyPredictions(),
      ])
      setMe(meData)
      setDisplayName(meData.displayName ?? '')
      setSubscription(sub)
      setPlans(planList)
      setPredictions(preds)
    } catch (e) {
      setError(e.message)
    }
  }

  useEffect(() => {
    load()
  }, [])

  async function saveProfile(e) {
    e.preventDefault()
    setMsg(null)
    setError(null)
    try {
      const updated = await api.updateMe({ displayName })
      setMe(updated)
      setMsg('Perfil actualizado.')
    } catch (e) {
      setError(e.message)
    }
  }

  async function subscribe(plan) {
    setMsg(null)
    setError(null)
    try {
      const sub = await api.subscribe({ plan })
      setSubscription(sub)
      setMsg(`Plan ${plan} activado.`)
    } catch (e) {
      setError(e.message)
    }
  }

  async function cancel() {
    setMsg(null)
    try {
      const sub = await api.cancelSubscription()
      setSubscription(sub)
      setMsg('Suscripción cancelada.')
    } catch (e) {
      setError(e.message)
    }
  }

  if (!me) {
    return <p className="text-slate-400">{error ?? 'Cargando…'}</p>
  }

  return (
    <section className="space-y-8">
      <div>
        <h1 className="text-2xl font-bold">Mi perfil</h1>
        {error && <p className="mt-2 text-red-400">{error}</p>}
        {msg && <p className="mt-2 text-pitch-400">{msg}</p>}
        <form onSubmit={saveProfile} className="mt-4 max-w-sm space-y-3">
          <p className="text-sm text-slate-400">
            Usuario: <span className="text-slate-200">{me.username}</span> ·{' '}
            {me.email}
          </p>
          <input
            className="w-full rounded-lg border border-slate-700 bg-slate-900 px-4 py-2"
            placeholder="Nombre visible"
            value={displayName}
            onChange={(e) => setDisplayName(e.target.value)}
          />
          <button className="rounded-lg bg-pitch-700 px-4 py-2 font-medium hover:bg-pitch-500">
            Guardar
          </button>
        </form>
      </div>

      <div>
        <h2 className="text-lg font-semibold">Suscripción</h2>
        <p className="mt-1 text-sm text-slate-400">
          Plan actual:{' '}
          <span className="font-semibold text-pitch-400">
            {subscription?.plan ?? 'FREE'}
          </span>
        </p>
        <div className="mt-4 grid gap-4 sm:grid-cols-3">
          {plans.map((plan) => (
            <div
              key={plan.plan}
              className="rounded-xl border border-slate-700 bg-slate-800/50 p-5"
            >
              <h3 className="text-base font-semibold">{plan.displayName}</h3>
              <p className="mt-1 text-2xl font-bold text-pitch-500">
                {plan.monthlyPriceEur.toFixed(2)} €
                <span className="text-xs font-normal text-slate-400">/mes</span>
              </p>
              <p className="mt-2 text-xs text-slate-400">{plan.description}</p>
              <button
                onClick={() => subscribe(plan.plan)}
                disabled={subscription?.plan === plan.plan}
                className="mt-3 w-full rounded-lg bg-pitch-700 py-1.5 text-sm font-medium hover:bg-pitch-500 disabled:opacity-40"
              >
                {subscription?.plan === plan.plan ? 'Plan actual' : 'Activar'}
              </button>
            </div>
          ))}
        </div>
        {subscription?.plan && subscription.plan !== 'FREE' && (
          <button
            onClick={cancel}
            className="mt-4 text-sm text-red-400 hover:underline"
          >
            Cancelar suscripción
          </button>
        )}
        <p className="mt-3 text-xs text-slate-500">
          Los planes solo desbloquean funciones lúdicas. No existen apuestas con
          dinero real.
        </p>
      </div>

      <div>
        <h2 className="text-lg font-semibold">Mis predicciones</h2>
        <div className="mt-4 overflow-hidden rounded-xl border border-slate-700">
          <table className="w-full text-left text-sm">
            <thead className="bg-slate-800 text-slate-400">
              <tr>
                <th className="px-4 py-2">Partido</th>
                <th className="px-4 py-2 text-center">Pronóstico</th>
                <th className="px-4 py-2 text-right">Puntos</th>
              </tr>
            </thead>
            <tbody>
              {predictions.length === 0 && (
                <tr>
                  <td colSpan="3" className="px-4 py-6 text-center text-slate-500">
                    Aún no has hecho predicciones.
                  </td>
                </tr>
              )}
              {predictions.map((p) => (
                <tr key={p.id} className="border-t border-slate-800">
                  <td className="px-4 py-2">#{p.matchId}</td>
                  <td className="px-4 py-2 text-center">
                    {p.predictedHomeScore} - {p.predictedAwayScore}
                  </td>
                  <td className="px-4 py-2 text-right">
                    {p.pointsAwarded ?? '—'}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </section>
  )
}
