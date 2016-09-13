/*
 * Copyright 2010-2016 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.ir.expressions.impl

import org.jetbrains.kotlin.ir.*
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrGetClass
import org.jetbrains.kotlin.ir.expressions.impl.IrExpressionBase
import org.jetbrains.kotlin.ir.visitors.IrElementVisitor
import org.jetbrains.kotlin.types.KotlinType

class IrGetClassImpl(startOffset: Int, endOffset: Int, type: KotlinType) : IrExpressionBase(startOffset, endOffset, type), IrGetClass {
    constructor(startOffset: Int, endOffset: Int, type: KotlinType, argument: IrExpression) : this(startOffset, endOffset, type) {
        this.argument = argument
    }

    private var argumentImpl: IrExpression? = null
    override var argument: IrExpression
        get() = argumentImpl!!
        set(value) {
            argumentImpl?.detach()
            argumentImpl = value
            value.setTreeLocation(this, CHILD_EXPRESSION_SLOT)
        }

    override fun getChild(slot: Int): IrElement? =
            when (slot) {
                CHILD_EXPRESSION_SLOT -> argument
                else -> null
            }

    override fun replaceChild(slot: Int, newChild: IrElement) {
        when (slot) {
            CHILD_EXPRESSION_SLOT -> argument = newChild.assertCast()
            else -> throwNoSuchSlot(slot)
        }
    }

    override fun <R, D> accept(visitor: IrElementVisitor<R, D>, data: D): R {
        return visitor.visitGetClass(this, data)
    }

    override fun <D> acceptChildren(visitor: IrElementVisitor<Unit, D>, data: D) {
        argument.accept(visitor, data)
    }

}