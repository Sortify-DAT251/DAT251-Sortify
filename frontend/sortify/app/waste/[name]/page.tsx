
// Creates a dynamic page for each waste item. 
// TODO: add info, type, map...
export default async function Page({
    params,
  }: {
    params: Promise<{ name: string }>
  }) {
    const { name } = await params
    return <div> 
        <h1>{name.toString()}</h1>
        <p>info ...</p>
        </div>
  }