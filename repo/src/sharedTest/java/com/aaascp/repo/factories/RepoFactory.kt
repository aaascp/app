package com.aaascp.repo.factories

import com.aaascp.repo.domain.Owner
import com.aaascp.repo.domain.Repo
import java.util.UUID
import kotlin.random.Random

private const val SEED = 0

fun createRandomRepo(randomGenerator: Random = Random(SEED)) = Repo(
    id = randomGenerator.nextLong(),
    name = UUID.randomUUID().toString(),
    fullName = UUID.randomUUID().toString(),
    description = UUID.randomUUID().toString(),
    url = UUID.randomUUID().toString(),
    stars = randomGenerator.nextInt(),
    forks = randomGenerator.nextInt(),
    language = UUID.randomUUID().toString(),
    owner = Owner(
        name = UUID.randomUUID().toString(),
        avatarUrl = UUID.randomUUID().toString()
    )
)
